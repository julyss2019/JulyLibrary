package com.github.julyss2019.mcsp.julylibrary.config;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class JulyConfig {
    /**
     * 载入配置
     * @param plugin
     * @param section
     * @param obj 被实例化的类
     */
    public static void reloadConfig(Plugin plugin, ConfigurationSection section, Object obj) {
        setFields(plugin, section, obj);
    }

    private static void setFields(Plugin plugin, ConfigurationSection section, Object obj) {
        Class<?> clazz = obj.getClass();

        // 反射获得所有变量
        for (Field field : clazz.getDeclaredFields()) {
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
                    } else if (fieldType == Sound.class) { // 对 Sound 类的支持
                        String soundName = section.getString(configPath);

                        try {
                            value = Sound.valueOf(soundName);
                        } catch (Exception e) {
                            plugin.getLogger().warning(clazz.getName() + " 中 " + configPath + " = " + soundName + " 不合法.");
                        }
                    } else if (fieldType == Material.class) {
                        String materialName = section.getString(configPath);

                        try {
                            value = Material.valueOf(materialName);
                        } catch (Exception e) {
                            plugin.getLogger().warning(clazz.getName() + " 中 " + configPath + " = " + materialName + " 不合法.");
                        }
                    } else {
                        value = section.get(configPath);
                    }

                    // 没值的情况
                    if (value == null || value instanceof MemorySection) {
                        continue;
                    }

                    // 设置允许访问
                    field.setAccessible(true);

                    try {
                        field.set(obj, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    field.setAccessible(false);
                } else {
                    plugin.getLogger().warning(clazz.getName() + " 中 " + configPath + " 不存在.");
                }
            }
        }
    }

    /**
     * 载入配置
     * @param section 节点
     * @param clazz 配置类（不需要实例化）
     * @return
     */
    public static Object loadConfig(Plugin plugin, ConfigurationSection section, Class<?> clazz) {
        Object obj = null;

        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        assert obj != null;
        setFields(plugin, section, obj);
        return obj;
    }
}
