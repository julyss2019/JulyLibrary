package com.github.julyss2019.mcsp.julylibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {
    public static boolean isInteger(String s) {
        return s.matches("[0-9]+");
    }

    public static int getIntegerFromStr(String s) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return 0;
    }
}
