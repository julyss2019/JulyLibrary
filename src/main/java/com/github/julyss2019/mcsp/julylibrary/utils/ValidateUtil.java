package com.github.julyss2019.mcsp.julylibrary.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValidateUtil {
    public static <T> T notNull(@Nullable T obj) {
        return notNull(obj, new RuntimeException("对象不能为null"));
    }

    public static <T> T notNull(@Nullable T obj, @NotNull RuntimeException e) {
        if (obj == null) {
            throw e;
        }

        return obj;
    }
}
