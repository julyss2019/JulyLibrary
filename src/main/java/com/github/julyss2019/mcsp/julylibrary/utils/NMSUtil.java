package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class NMSUtil {
    public static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    /**
     * 得到NMS类
     * @param name 类名
     * @return
     */
    public static Class<?> getNMSClass(@NotNull String name) {
        try
        {
            return Class.forName("net.minecraft.server." + SERVER_VERSION + "." + name);
        } catch (Exception ignored) {}

        return null;
    }
}
