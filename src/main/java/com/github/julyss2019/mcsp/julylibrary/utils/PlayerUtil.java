package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class PlayerUtil {
    private static Class<?> packetClass;

    static {
        packetClass = NMSUtil.getNMSClass("Packet");
    }

    public static boolean sendPacket(Player player, @NotNull Object packet) {
        try
        {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);

            playerConnection.getClass().getMethod("sendPacket", packetClass).invoke(playerConnection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean isOnline(Player player) {
        return player != null && player.isOnline();
    }

    public static boolean isOnline(String playerName) {
        Player player = Bukkit.getPlayer(playerName);

        return player != null && player.isOnline();
    }
}
