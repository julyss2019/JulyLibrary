package com.github.julyss2019.mcsp.julylibrary.utils;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class FileUtil {
    public static StringBuilder readLines(@NotNull File file) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append(SystemUtil.LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb;
    }

    public static String getFileName(@NotNull File file) {
        String tmp = file.getName();

        return tmp.substring(0, tmp.lastIndexOf("."));
    }
}
