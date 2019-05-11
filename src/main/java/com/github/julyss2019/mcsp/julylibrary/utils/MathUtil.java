package com.github.julyss2019.mcsp.julylibrary.utils;

public class MathUtil {
    public static int getRandomInteger(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min 必须<= max");
        }

        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
