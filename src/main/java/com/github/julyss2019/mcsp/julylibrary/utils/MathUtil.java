package com.github.julyss2019.mcsp.julylibrary.utils;

public class MathUtil {
    /**
     * 得到随机数
     * @param min 包含
     * @param max 包含
     * @return
     */
    public static int getRandomInteger(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min 必须<= max");
        }

        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
