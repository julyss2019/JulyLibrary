package com.github.julyss2019.mcsp.julylibrary.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static List<String> replacePlaceholders(List<String> strList, Map<String, String> placeholderMap) {
        List<String> result = new ArrayList<>();

        for (String str : strList) {
            String tmp = str;

            for (Map.Entry<String, String> entry : placeholderMap.entrySet()) {
                tmp = tmp.replace(entry.getKey(), entry.getValue());
            }

            result.add(tmp);
        }

        return result;
    }
}
