package com.github.julyss2019.mcsp.julylibrary.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Deprecated
public class ListUtil {
    @Deprecated
    public static List<String> toLowercaseList(List<String> list) {
        List<String> newList = new ArrayList<>();

        for (String s : list) {
            newList.add(s.toLowerCase());
        }

        return newList;
    }

    @Deprecated
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
