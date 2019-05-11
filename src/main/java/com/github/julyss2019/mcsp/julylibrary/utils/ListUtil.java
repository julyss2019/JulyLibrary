package com.github.julyss2019.mcsp.julylibrary.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    public static List<String> toLowercaseList(List<String> list) {
        List<String> newList = new ArrayList<>();

        for (String s : list) {
            newList.add(s.toLowerCase());
        }

        return newList;
    }
}
