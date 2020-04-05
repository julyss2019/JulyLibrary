package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.plugin.Plugin;
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

    /**
     * 创建jar包内的资源文件（如果不存在）
     * @param fileName
     * @param outFile
     */
    public static void saveResourceFile(@NotNull Plugin plugin, @NotNull String fileName, @NotNull File outFile, boolean replace) {
        File outParentFile = outFile.getParentFile();

        // 创建父文件夹
        if (!outParentFile.exists() && !outParentFile.mkdirs()) {
            throw new RuntimeException("创建文件夹失败: " + outParentFile.getAbsolutePath());
        }

        if (!outFile.exists() || replace) {
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = plugin.getResource(fileName);

                if (in == null) {
                    throw new RuntimeException("文件不存在: " + fileName);
                }

                out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                out.close();
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
