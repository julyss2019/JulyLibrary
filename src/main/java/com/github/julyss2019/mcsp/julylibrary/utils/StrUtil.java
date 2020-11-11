package com.github.julyss2019.mcsp.julylibrary.utils;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class StrUtil {
    public static boolean isInteger(@NotNull String s) {
        return s.matches("[0-9]+");
    }

    @Deprecated
    public static int getIntegerFromStr(@NotNull String s) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return 0;
    }

    public static int getIntegerFromStr(@NotNull String s, int def) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return def;
    }
}
