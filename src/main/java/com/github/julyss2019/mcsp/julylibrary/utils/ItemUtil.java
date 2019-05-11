package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    /**
     * 判断物品是否有效（不为null且meta不为null）
     * @param itemStack
     * @return
     */
    public static boolean isValidItem(ItemStack itemStack) {
        return itemStack != null && itemStack.getItemMeta() != null;
    }

    /**
     * 判断物品是否包含指定lore
     * @param itemStack
     * @param lore
     * @return
     */
    public static boolean containsLore(ItemStack itemStack, String lore) {
        if (!isValidItem(itemStack)) {
            return false;
        }

        List<String> lores = itemStack.getItemMeta().getLore();

        return lores != null && lores.contains(lore);
    }

    /**
     * 得到物品lore
     * @param itemStack
     * @return 不为空的`List
     */
    public static List<String> getLores(ItemStack itemStack) {
        if (!isValidItem(itemStack)) {
            return new ArrayList<>();
        }

        List<String> lores = itemStack.getItemMeta().getLore();

        return lores == null ? new ArrayList<>() : lores;
    }
}
