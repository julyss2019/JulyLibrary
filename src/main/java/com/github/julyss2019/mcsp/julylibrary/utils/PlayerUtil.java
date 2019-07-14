package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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

    public interface ItemFilter {
        boolean filter(ItemStack itemStack);
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

    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1f,1f);
    }

    public static boolean hasItem(Player player, ItemFilter itemFilter, int amount) {
        int totalAmount = 0;
        ItemStack[] items = player.getInventory().getContents();

        for (int i = 0; i < items.length && totalAmount < amount; i++) {
            ItemStack itemStack = items[i];

            if (itemFilter.filter(itemStack)) {
                totalAmount += itemStack.getAmount();
            }
        }

        return totalAmount >= amount;
    }

    public static boolean takeItems(Player player, ItemFilter itemFilter, int takeAmount) {
        PlayerInventory playerInventory = player.getInventory();
        int tookAmount = 0;

        for (int i = 0; i < 36 && tookAmount < takeAmount; i++) {
            ItemStack itemStack = playerInventory.getItem(i);

            if (itemFilter.filter(itemStack)) {
                int itemAmount = itemStack.getAmount();

                // 如果物品的数量超过剩下需要的数量
                if (itemAmount > (takeAmount - tookAmount)) {
                    int take = takeAmount - tookAmount;

                    itemStack.setAmount(itemAmount - take);
                    tookAmount += take;
                } else {
                    // 小于等于需要的数量
                    tookAmount += itemAmount;
                    itemStack.setType(Material.AIR);
                }

                playerInventory.setItem(i, itemStack);
            }
        }

        return tookAmount >= takeAmount;
    }
}
