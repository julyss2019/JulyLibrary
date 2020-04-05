package com.github.julyss2019.mcsp.julylibrary.text;

import org.jetbrains.annotations.NotNull;

public class DateTimeUnit {
    public static final DateTimeUnit SIMPLE_CHINESE_UNIT = new DateTimeUnit("年", "月", "日", "时", "分", "秒");

    private String yearUnit;
    private String monthUnit;
    private String dayUnit;
    private String hourUnit;
    private String minuteUnit;
    private String secondUnit;

    public DateTimeUnit(@NotNull String yearUnit, @NotNull String monthUnit, @NotNull String dayUnit, @NotNull String hourUnit, @NotNull String minuteUnit, @NotNull String secondUnit) {
        this.yearUnit = yearUnit;
        this.monthUnit = monthUnit;
        this.dayUnit = dayUnit;
        this.hourUnit = hourUnit;
        this.minuteUnit = minuteUnit;
        this.secondUnit = secondUnit;
    }

    public String getYearUnit() {
        return yearUnit;
    }

    public String getMonthUnit() {
        return monthUnit;
    }

    public String getDayUnit() {
        return dayUnit;
    }

    public String getHourUnit() {
        return hourUnit;
    }

    public String getMinuteUnit() {
        return minuteUnit;
    }

    public String getSecondUnit() {
        return secondUnit;
    }
}
