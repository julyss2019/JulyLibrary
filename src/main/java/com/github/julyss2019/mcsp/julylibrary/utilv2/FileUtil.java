package com.github.julyss2019.mcsp.julylibrary.utilv2;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    /**
     * 读取文件
     * @param file
     * @return
     */
    public static List<String> readLinesV2(@NotNull File file) {
        List<String> lines = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }

    /**
     * 获取文件名
     * @param file
     * @return
     */
    public static String getFileName(@NotNull File file) {
        String tmp = file.getName();

        return tmp.substring(0, tmp.lastIndexOf("."));
    }
}
