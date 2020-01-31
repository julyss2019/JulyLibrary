package com.github.julyss2019.mcsp.julylibrary.message;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JulyText {
    /**
     * List<String> 着色
     * @param messages
     * @return
     */
    public static List<String> getColoredTexts(@NotNull List<String> messages) {
        List<String> result = new ArrayList<>();

        messages.forEach(s -> result.add(getColoredText(s)));
        return result;
    }

    /**
     * 文字着色
     * @param s
     * @return
     */
    public static String getColoredText(@NotNull String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
