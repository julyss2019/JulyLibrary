package com.github.julyss2019.mcsp.julylibrary.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class JulyConfig {
    @Deprecated
    public static void reloadConfig(Plugin plugin, ConfigurationSection section, Object obj) {
        setFields(plugin, section, obj);
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
    public static void loadConfig(Plugin plugin, ConfigurationSection section, Object obj) {
        setFields(plugin, section, obj);
    }

    /**
     * 载入配置
     * @param plugin 插件实例
     * @param section 节点
     * @param clazz 配置类（不需要实例化）
     * @return
     */
    public static <T> T loadConfig(Plugin plugin, ConfigurationSection section, Class<T> clazz) {
        T obj;

        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        setFields(plugin, section, obj);
        return obj;
    }

    private static void setFields(Plugin plugin, ConfigurationSection section, Object obj) {
        Class<?> clazz = obj.getClass();

        // 反射获得所有变量
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();

            // 查看是否有Config注解
            if (field.isAnnotationPresent(Config.class)) {
                Config configAnnotation = field.getAnnotation(Config.class);
                String configPath = configAnnotation.path();

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
                                    throw new RuntimeException(e);
                                }
                            }
                        } else {
                            value = section.get(configPath);
                        }
                    }

                    // 没有有效值
                    if (value == null || value instanceof MemorySection) {
                        plugin.getLogger().warning(clazz.getName() + " 中的变量 " + fieldName + "(" + configPath + ")" + " 未能被成功赋值, 因为找不到合适的值.");
                        continue;
                    }

                    // 设置允许访问
                    field.setAccessible(true);

                    try {
                        field.set(obj, value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                    field.setAccessible(false);
                } else {
                    plugin.getLogger().warning(clazz.getName() + " 中的变量 " + fieldName + "(" + configPath + ")" + " 未能被成功赋值, 因为路径不存在.");
                }
            }
        }
    }
}
