package com.github.julyss2019.mcsp.julylibrary.config;

import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;

public class JulyConfig {
    /**
     * 载入配置
     * @param section 节点
     * @param clazz 配置类（不需要实例化）
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static JulyConfig loadConfig(ConfigurationSection section, Class<?> clazz) throws IllegalAccessException, InstantiationException {
        /*
            clazz只是个能被实例化而未被实例化的Class
         */
        JulyConfig obj = (JulyConfig) clazz.newInstance(); // 新的对象

        // 反射获得所有变量
        for (Field field : clazz.getDeclaredFields()) {
            // 查看是否有Config注解
            if (field.isAnnotationPresent(Config.class)) {
                Config configAnnotation = field.getAnnotation(Config.class);
                String configPath = configAnnotation.path();

                // 如果yml有目标项
                if (section.contains(configPath)) {
                    Object value;

                    if (field.getType() == short.class) {
                        value = (short) section.getInt(configAnnotation.path());
                    } else {
                        value = section.get(configAnnotation.path());
                    }

                    // 设置允许访问
                    field.setAccessible(true);
                    field.set(obj, value);
                    field.setAccessible(false);
                }
            }
        }

        return obj;
    }
}
