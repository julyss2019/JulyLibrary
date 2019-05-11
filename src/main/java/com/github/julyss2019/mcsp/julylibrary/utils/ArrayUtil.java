package com.github.julyss2019.mcsp.julylibrary.utils;

import java.util.Arrays;

public class ArrayUtil {
    /**
     * 从String数组删除指定索引的元素
     * @param arr 数组
     * @param index 索引，从0开始
     * @return
     */
    public static String[] removeElementFromStrArray(String[] arr, int index) {
        String[] newArr = Arrays.copyOf(arr, arr.length);

        for (int i = 0; i < newArr.length - index - 1; i++) {
            newArr[index + i] = newArr[index + i + 1];
        }

        return Arrays.copyOf(newArr, newArr.length - 1);
    }
}
