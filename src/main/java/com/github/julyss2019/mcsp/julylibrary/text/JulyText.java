package com.github.julyss2019.mcsp.julylibrary.text;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class JulyText {
    public static List<String> setPlaceholders(@NotNull List<String> list, @NotNull PlaceholderContainer placeholderContainer) {
        return list.stream().map(s -> setPlaceholders(s, placeholderContainer)).collect(Collectors.toList());
    }

    /**
     * 替换占位符（支持转义），格式：${placeholder}，转义 $${placeholder}
     * @param s
     * @param placeholderContainer 占位符容器
     * @return
     */
    public static String setPlaceholders(@NotNull String s, @NotNull PlaceholderContainer placeholderContainer) {
        return setPlaceholders(s, placeholderContainer.toMap());
    }

    @Deprecated
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

                if (nextIndex == len) {
                    result.append(currentChar);
                    continue;
                }

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
                        result.append("$");
                        break;
                }
            } else {
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    public static Set<String> getPlaceholders(@NotNull String s) {
        Set<String> result = new HashSet<>();

        int len = s.length();

        for (int i = 0; i < len; i++) {
            char currentChar = s.charAt(i);

            if (currentChar == '$') {
                int nextIndex = i + 1;

                if (nextIndex == len) {
                    continue;
                }

                char nextChar = s.charAt(nextIndex);

                switch (nextChar) {
                    case '{':
                        int right = s.indexOf("}", nextIndex);

                        if (right != -1) {
                            String placeholder = s.substring(nextIndex + 1, right);

                            result.add(placeholder);
                        } else {
                            i++;
                            break;
                        }

                        break;
                    case '$':
                        i++;
                        break;
                    default:
                        break;
                }
            }
        }

        return result;
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
    @Deprecated
    public static List<String> getColoredTexts(@NotNull Collection<String> messages) {
        List<String> result = new ArrayList<>();

        messages.forEach(s -> result.add(getColoredText(s)));
        return result;
    }

    public static Set<String> getColoredTexts(@NotNull Set<String> texts) {
        return texts.stream().map(JulyText::getColoredText).collect(Collectors.toSet());
    }

    public static List<String> getColoredTexts(@NotNull List<String> texts) {
        return texts.stream().map(JulyText::getColoredText).collect(Collectors.toList());
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
