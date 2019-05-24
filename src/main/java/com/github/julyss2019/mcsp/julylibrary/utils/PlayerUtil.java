package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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

    /**
     * 得到玩家空闲的背包格子数量
     * @param player
     * @return
     */
    public static int getInventoryFreeSize(Player player) {
        int total = 0;
        PlayerInventory playerInventory = player.getInventory();

        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = playerInventory.getItem(i);

            if (itemStack == null || itemStack.getType() == Material.AIR) {
                total += 1;
            }
        }
        return total;
    }
}
