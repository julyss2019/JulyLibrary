package com.github.julyss2019.mcsp.julylibrary.config;

import com.github.julyss2019.mcsp.julylibrary.JulyLibraryLogger;
import com.github.julyss2019.mcsp.julylibrary.config.validate.Max;
import com.github.julyss2019.mcsp.julylibrary.config.validate.Min;
import com.github.julyss2019.mcsp.julylibrary.config.validate.NotEmpty;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JulyConfig {
    /**
     * 载入配置
     * @param section 节点
     * @param obj 对象实例
     */
    public static void loadConfig(@NotNull ConfigurationSection section, @NotNull Object obj) {
        setFields(section, obj);
    }

    /**
     * 载入配置
     * @param section 节点
     * @param clazz 配置类（不需要实例化）
     * @return
     */
    public static <T> T loadConfig(@NotNull ConfigurationSection section, @NotNull Class<T> clazz) {
        T obj;

        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        setFields(section, obj);
        return obj;
    }

    private static void setFields(@NotNull ConfigurationSection section, @NotNull Object obj) {
        Class<?> clazz = obj.getClass();
        Set<String> errors = new HashSet<>();

        // 反射获得所有变量
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();

            // 查看是否有Config注解
            if (field.isAnnotationPresent(Config.class)) {
                Config configAnnotation = field.getAnnotation(Config.class);
                String configPath = configAnnotation.path();
                Set<String> fieldErrors = new HashSet<>();

                // 如果yml有目标项
                if (section.contains(configPath)) {
                    Object value = null;
                    Class<?> fieldType = field.getType();

                    if (fieldType == short.class) {
                        value = (short) section.getInt(configAnnotation.path());
                    } else {
                        // 对 java.lang.Enum 类的支持
                        Object[] enumConstants = fieldType.getEnumConstants();

                        if (enumConstants != null && fieldType.getEnumConstants().length > 0) {
                            for (Object enumObj : fieldType.getEnumConstants()) {
                                try {
                                    if (enumObj.getClass().getMethod("name").invoke(enumObj).equals(section.getString(configPath))) {
                                        value = enumObj;
                                    }
                                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException("设置配置 " + configPath + " 时发生了异常", e);
                                }
                            }
                        } else {
                            value = section.get(configPath);
                        }
                    }

                    // NotNull 校验
                    if (field.isAnnotationPresent(com.github.julyss2019.mcsp.julylibrary.config.validate.NotNull.class)) {
                        if (value == null) {
                            fieldErrors.add("配置 " + configPath + " 不能为 null.");
                        }
                    }

                    // NotEmpty 校验
                    if (field.isAnnotationPresent(NotEmpty.class)) {
                        if (field.getType() != List.class) {
                            throw new RuntimeException("类 " + obj.getClass().getName() + " 变量 " + field.getName() + " 类型不匹配.");
                        }

                        if (value == null) {
                            fieldErrors.add("配置 " + configPath + " 不能为 null.");
                        } else {
                            if (!(value instanceof List)) {
                                fieldErrors.add("配置 " + configPath + " 必须为 List.");
                            } else {
                                List list = (List) value;

                                if (list.isEmpty()) {
                                    fieldErrors.add("配置 " + configPath + " 必须至少有一个元素.");
                                }
                            }
                        }
                    }

                    if (field.isAnnotationPresent(Min.class) || field.isAnnotationPresent(Max.class)) {
                        // 变量类型校验
                        if (fieldType != int.class
                                && fieldType != Integer.class
                                && fieldType != short.class
                                && fieldType != Short.class
                                && fieldType != long.class
                                && fieldType != Long.class
                                && fieldType != double.class
                                && fieldType != Double.class
                                && fieldType != float.class
                                && fieldType != Float.class) {
                            throw new RuntimeException("类 " + obj.getClass().getName() + " 变量 " + field.getName() + " 类型不匹配.");
                        }

                        // Min 校验 int 级别
                        if (field.isAnnotationPresent(Min.class)) {
                            Min min = field.getAnnotation(Min.class);
                            int intValue = NumberConversions.toInt(value);

                            if (intValue < min.value()) {
                                fieldErrors.add("配置 " + configPath + " 允许的最小值为 " + min.value() + ".");
                            }
                        }

                        // Max 校验 int 级别
                        if (field.isAnnotationPresent(Max.class)) {
                            Max max = field.getAnnotation(Max.class);
                            int intValue = NumberConversions.toInt(value);

                            if (intValue > max.value()) {
                                fieldErrors.add("配置 " + configPath + " 允许的最大值为 " + max.value() + ".");
                            }
                        }
                    }

                    // 有错误的情况
                    if (!fieldErrors.isEmpty()) {
                        errors.addAll(fieldErrors);
                        continue;
                    }

                    field.setAccessible(true);

                    try {
                        field.set(obj, value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("设置配置 " + configPath + " 时发生了异常", e);
                    }

                    field.setAccessible(false);
                } else {
                    errors.add(clazz.getName() + " 中的变量 " + fieldName + "(" + configPath + ")" + " 未能被成功赋值, 因为路径不存在.");
                }
            }
        }

        if (!errors.isEmpty()) {
            errors.forEach(JulyLibraryLogger::error);
            throw new RuntimeException("配置存在异常");
        }
    }

    @Deprecated
    public static void reloadConfig(Plugin plugin, ConfigurationSection section, Object obj) {
        setFields(section, obj);
    }

    /**
     * 载入配置
     * 不存在的值用 defaultSection 代替
     * @param plugin
     * @param section
     * @param defaultSection
     * @param clazz
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> T loadConfig(Plugin plugin, ConfigurationSection section, ConfigurationSection defaultSection, Class<T> clazz) {
        T result = loadConfig(plugin, defaultSection, clazz);

        loadConfig(plugin, section, result);
        return result;
    }

    /**
     * 载入配置
     * @param plugin 插件实例
     * @param section 节点
     * @param obj 对象实例
     */
    @Deprecated
    public static void loadConfig(Plugin plugin, ConfigurationSection section, Object obj) {
        setFields(section, obj);
    }

    /**
     * 载入配置
     * @param plugin 插件实例
     * @param section 节点
     * @param clazz 配置类（不需要实例化）
     * @return
     */
    @Deprecated
    public static <T> T loadConfig(Plugin plugin, ConfigurationSection section, Class<T> clazz) {
        T obj;

        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        setFields(section, obj);
        return obj;
    }
}
