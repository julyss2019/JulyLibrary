package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
/*
NMS 版本对应表：
1.7.2 = 1_7_R1
1.7.5 = 1_7_R2
1.7.8 = 1_7_R3
1.7.10 = 1_7_R4
1.8 = 1_8_R1
1.8.3 = 1_8_R2
1.8.8 = 1_8_R3
1.9.2 = 1_9_R1
1.9.4 = 1_9_R2
1.10.2 = 1_10_R1
1.11.2 = 1_11_R1

https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions/
 */

public class NMSUtil {
    @Deprecated
    public static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

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
