package com.github.julyss2019.mcsp.julylibrary.utils;

import com.github.julyss2019.mcsp.julylibrary.text.DateTimeUnit;
import com.github.julyss2019.mcsp.julylibrary.text.JulyText;

import java.text.SimpleDateFormat;

@Deprecated
public class TimeUtil {
    /**
     * yyyy/MM/dd
     */
    @Deprecated
    public static SimpleDateFormat YMD_SDF = new SimpleDateFormat("yyyy/MM/dd");
    /**
     * yyyy/MM/dd HH:mm
     */
    @Deprecated
    public static SimpleDateFormat YMDHM_SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    /**
     * yyyy/MM/dd HH:mm:ss
     */
    @Deprecated
    public static SimpleDateFormat YMDHMS_SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * 将秒转换为时分秒
     * @param timeLeft
     * @return
     */
    @Deprecated
    public static String getTimeLeftStr(long timeLeft) {
        return JulyText.secondToStr(timeLeft, DateTimeUnit.SIMPLE_CHINESE_UNIT);
    }
}
