package com.github.julyss2019.mcsp.julylibrary.placeholder;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderText {
    /**
     * 替换占位符
     * @param s
     * @param placeholderContainer
     * @return
     */
    public static String setPlaceholders(@NotNull String s, @NotNull PlaceholderContainer placeholderContainer) {
        String result = s;

        for (Map.Entry<String, String> entry : placeholderContainer.getPlaceholderMap().entrySet()) {
            // 对正则符号进行转义
            result = ignoreCaseReplace(result, entry.getKey()
                    .replace("\\", "\\\\").replace("*", "\\*")
                    .replace("+", "\\+").replace("|", "\\|")
                    .replace("{", "\\{").replace("}", "\\}")
                    .replace("(", "\\(").replace(")", "\\)")
                    .replace("^", "\\^").replace("$", "\\$")
                    .replace("[", "\\[").replace("]", "\\]")
                    .replace("?", "\\?").replace(",", "\\,")
                    .replace(".", "\\.").replace("&", "\\&"), entry.getValue());
        }

        return result;
    }

    /**
     * 忽略大小写的替换
     * @param source
     * @param target
     * @param replacement
     * @return
     */
    private static String ignoreCaseReplace(String source, String target, String replacement) {
        Pattern p = Pattern.compile(target, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);

        return m.replaceAll(replacement);
    }
}
