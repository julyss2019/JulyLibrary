package com.github.julyss2019.mcsp.julylibrary.utils;

import java.io.*;

public class FileUtil {
    public static StringBuilder readLines(File file) {
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
}
