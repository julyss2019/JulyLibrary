package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    @Deprecated
    public static String readLines(@NotNull File file) {
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

        return sb.toString();
    }

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

    @Deprecated
    public static void saveResourceFile(@NotNull Plugin plugin, @NotNull String fileName, @NotNull File outFile, boolean replace) {
        PluginUtil.saveResource(plugin, fileName, outFile, replace);
    }
}
