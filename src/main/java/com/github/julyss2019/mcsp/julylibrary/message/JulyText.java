package com.github.julyss2019.mcsp.julylibrary.message;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Deprecated
public class JulyText {
    /**
     * 替换占位符（支持转义），格式：${placeholder}，转义 $${placeholder}
     * @param s
     * @param placeholderMap 占位符Map 区分大小写
     * @return
     */
    public static String setPlaceholders(@NotNull String s, @NotNull Map<String, String> placeholderMap) {
        int len = s.length();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < len; i++) {
            char currentChar = s.charAt(i);

            if (currentChar == '$') {
                int nextIndex = i + 1;
                char nextChar = s.charAt(nextIndex);

                switch (nextChar) {
                    case '{':
                        int right = s.indexOf("}", nextIndex);

                        if (right != -1) {
                            String placeholder = s.substring(nextIndex + 1, right);

                            if (placeholderMap.containsKey(placeholder)) {
                                result.append(placeholderMap.get(placeholder));
                                i += placeholder.length() + 2; // { } 为两个字符，length() 即指向了下一个索引
                                break;
                            } else {
                                result.append("$");
                            }
                        } else {
                            result.append("{");
                            i++;
                            break;
                        }

                        break;
                    case '$':
                        result.append("$");
                        i++;
                        break;
                    default:
                        result.append(nextChar);
                        break;
                }
            } else {
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    /**
     * 秒转换为 年月日时分秒
     * @param seconds 秒
     * @return 例子：1年1月1时1分1秒
     */
    public static String secondToStr(long seconds, @NotNull DateTimeUnit dateTimeUnit) {
        if (seconds < 0) {
            throw new RuntimeException("秒数必须大于等于0");
        }

        long leftSeconds = seconds;

        long years = leftSeconds / 60L / 60L / 24L / 365L;

        leftSeconds = leftSeconds % (60L * 60L * 24L * 365L);

        long months = leftSeconds / 30L / 24L / 60L / 60L;

        leftSeconds = leftSeconds % (30L * 24L * 60L * 60L);

        long days = leftSeconds / 24L / 60L / 60L;

        leftSeconds = leftSeconds % (24L * 60L * 60L);

        long hours = leftSeconds / 60L / 60L;

        leftSeconds = leftSeconds % (60L * 60L);

        long minutes = leftSeconds / 60L;

        leftSeconds = leftSeconds % 60;

        StringBuilder stringBuilder = new StringBuilder();

        if (years != 0) {
            stringBuilder.append(years).append(dateTimeUnit.getYearUnit());
        }

        if (months != 0) {
            stringBuilder.append(months).append(dateTimeUnit.getMonthUnit());
        }

        if (days != 0) {
            stringBuilder.append(days).append(dateTimeUnit.getDayUnit());
        }

        if (hours != 0) {
            stringBuilder.append(hours).append(dateTimeUnit.getHourUnit());
        }

        if (minutes != 0) {
            stringBuilder.append(minutes).append(dateTimeUnit.getMinuteUnit());
        }

        // 如果前面一个单位都没有，则补0
        if (stringBuilder.length() == 0 || leftSeconds != 0) {
            stringBuilder.append(leftSeconds).append(dateTimeUnit.getSecondUnit());
        }

        return stringBuilder.toString();
    }

    /**
     * List<String> 着色
     * @param messages
     * @return
     */
    public static List<String> getColoredTexts(@NotNull Collection<String> messages) {
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
