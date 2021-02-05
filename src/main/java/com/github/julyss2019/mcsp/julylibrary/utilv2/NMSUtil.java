package com.github.julyss2019.mcsp.julylibrary.utilv2;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class NMSUtil {
    public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final int[] CURRENT_VERSION_NUMBER_ARRAY = getVersionNumberArray(NMS_VERSION);

    /**
     * 比较版本
     * @param version 版本，e.g. v1_15_R1
     * @return version version > 服务端版本：返回 1；version < 服务端版本：返回 -1；一样返回 0
     */
    public static int compareVersion(@NotNull String version) {
        int[] targetVersionNumberArray = getVersionNumberArray(version);

        for (int i = 0; i < 3; i++) {
            if (targetVersionNumberArray[i] < CURRENT_VERSION_NUMBER_ARRAY[i]) {
                return 1;
            }

            if (targetVersionNumberArray[i] > CURRENT_VERSION_NUMBER_ARRAY[i]) {
                return -1;
            }
        }

        return 0;
    }

    private static int[] getVersionNumberArray(@NotNull String version) {
        if (!version.matches("v[0-9]+_[0-9]+_R[0-9]+")) {
            throw new RuntimeException("版本表达式不合法");
        }

        String[] versionStrArray = version.split("_");

        return new int[] {Integer.parseInt(versionStrArray[0].substring(1)), Integer.parseInt(versionStrArray[1]), Integer.parseInt(versionStrArray[2].substring(1))};
    }

    /**
     * 得到NMS类
     * @param name 类名
     * @return
     */
    public static Class<?> getNMSClass(@NotNull String name) {
        try
        {
            return Class.forName("net.minecraft.server." + NMS_VERSION + "." + name);
        } catch (Exception ignored) {}

        return null;
    }
}
