package com.github.julyss2019.mcsp.julylibrary.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class ValidateUtil {
    /**
     * 验证集合是否有 null 元素
     * 如果有则抛出异常
     * @param obj
     * @param <T>
     * @return
     */
    public static <T extends Collection<?>> T notNullElement(@NotNull T obj) {
        return notNullElement(obj, new RuntimeException("存在 null 元素"));
    }

    /**
     * 验证集合是否有 null 元素
     * 如果有则抛出异常
     * @param obj
     * @param e
     * @param <T>
     * @return
     */
    public static <T extends Collection<?>> T notNullElement(@NotNull T obj, @Nullable RuntimeException e) {
        if (obj.contains(null)) {
            throw new RuntimeException(e);
        }

        return obj;
    }

    /**
     * 验证 Map 是否有 null KV
     * 如果有则抛出异常
     * @param obj
     * @param <T>
     * @return
     */
    public static <T extends Map<?, ?>> T notNullEntry(@NotNull T obj) {
        return notNullEntry(obj, new RuntimeException("Map 存在 null KV"));
    }

    /**
     * 验证 Map 是否有 null KV
     * 如果有则抛出异常
     * @param map
     * @param e 异常
     * @param <T>
     * @return
     */
    public static <T extends Map<?, ?>> T notNullEntry(@NotNull T map, @Nullable RuntimeException e) {
        if (map.containsKey(null) || map.containsValue(null)) {
            throw e;
        }

        return map;
    }

    /**
     * 验证对象是否为 null
     * 如果为 null 抛出异常
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T notNull(@Nullable T obj) {
        return notNull(obj, new RuntimeException("存在 null 对象"));
    }

    /**
     * 验证对象是否为 null
     * 如果为 null 抛出异常
     * @param obj
     * @param e 异常
     * @param <T>
     * @return
     */
    public static <T> T notNull(@Nullable T obj, @NotNull RuntimeException e) {
        if (obj == null) {
            throw e;
        }

        return obj;
    }
}
