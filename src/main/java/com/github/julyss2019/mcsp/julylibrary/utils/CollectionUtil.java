package com.github.julyss2019.mcsp.julylibrary.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Deprecated
public class CollectionUtil {
    @Deprecated
    public static <T extends Collection<String>> List<String> toLowercase(T collection) {
        List<String> result = new ArrayList<>();

        collection.forEach(s -> result.add(s.toLowerCase()));
        return result;
    }

    @Deprecated
    public static <T extends Collection<String>> List<String> replaceStrings(T collection, Map<String, String> replaceMap) {
        List<String> result = new ArrayList<>();

        collection.forEach(s -> {
            String tmp = s;

            for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
                tmp = tmp.replace(entry.getKey(), entry.getValue());
            }

            result.add(tmp);
        });

        return result;
    }
}
