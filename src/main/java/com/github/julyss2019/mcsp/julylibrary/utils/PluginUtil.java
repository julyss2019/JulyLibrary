package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Deprecated
public class PluginUtil {
    /**
     * 创建jar包内的资源文件（如果不存在）
     * @param fileName
     * @param outFile
     */
    public static void saveResource(@NotNull Plugin plugin, @NotNull String fileName, @NotNull File outFile, boolean replace) {
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
