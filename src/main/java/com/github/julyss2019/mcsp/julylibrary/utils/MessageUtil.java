package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class MessageUtil {
    /**
     * 翻译颜色代码
     * @param s 文本
     * @return
     */
    public static String translateColorCode(String s) {
        return s == null ? null : ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> translateColorCode(List<String> list) {
        List<String> result = new ArrayList<>();

        if (list != null) {
            for (String s : list) {
                result.add(translateColorCode(s));
            }
        }

        return result;
    }
}
