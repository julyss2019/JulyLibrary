package com.github.julyss2019.mcsp.julylibrary.utilv2;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemUtil {
    public @interface ValidItem {}

    private static int getTypeId(@NotNull ItemStack itemStack) {
        try {
            return (int) itemStack.getClass().getDeclaredMethod("getTypeId").invoke(itemStack);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 得到物品ID
     * @param itemStack
     * @return
     */
    public static String getId(@NotNull ItemStack itemStack) {
        int id = getTypeId(itemStack);
        short data = itemStack.getDurability();

        return id + (data == 0 ? "" : ":" + data);
    }

    /**
     * 判断物品是否有效（不为null且meta不为null）
     * @param itemStack
     * @return
     */
    public static boolean isValid(@Nullable ItemStack itemStack) {
        return itemStack != null && itemStack.getItemMeta() != null;
    }

    /**
     * 得到物品指定索引的lore
     * @param itemStack 允许无效物品
     * @param index
     * @return
     */
    public static String getLore(@ValidItem ItemStack itemStack, int index) {
        if (index < 0) {
            throw new RuntimeException("索引不合法: " + index);
        }

        if (!isValid(itemStack)) {
            return null;
        }

        List<String> lores = itemStack.getItemMeta().getLore();

        if (lores == null) {
            return null;
        }

        if (index >= lores.size()) {
            return null;
        }

        return lores.get(index);
    }

    /**
     * 判断物品是否包含指定lore
     * @param itemStack 允许无效物品
     * @param lore
     * @return
     */
    public static boolean containsLore(@ValidItem ItemStack itemStack, @NotNull String lore) {
        if (!isValid(itemStack)) {
            return false;
        }

        List<String> lores = itemStack.getItemMeta().getLore();

        return lores != null && lores.contains(lore);
    }

    /**
     * 判断物品是否包含集合中的lore（一个即可）
     * @param itemStack
     * @param lores
     * @return
     */
    public static boolean containsOneLore(@ValidItem ItemStack itemStack, @NotNull Collection<String> lores) {
        if (!isValid(itemStack)) {
            return false;
        }

        ValidateUtil.notNullElement(lores, new RuntimeException("lores 中含有空元素"));

        if (lores.size() == 0) {
            return false;
        }

        List<String> itemLores = itemStack.getItemMeta().getLore();

        if (itemLores == null) {
            return false;
        }

        for (String itemLore : itemLores) {
            if (lores.contains(itemLore)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 得到物品lore
     * @param itemStack 允许无效物品
     * @return 不为null的List
     */
    public static List<String> getLores(ItemStack itemStack) {
        if (!isValid(itemStack)) {
            return new ArrayList<>();
        }

        List<String> lores = itemStack.getItemMeta().getLore();

        return lores == null ? new ArrayList<>() : lores;
    }

    /**
     * 减去物品的一个数量 如果-1后数量为0则返回null
     * @param itemStack
     * @return
     */
    public static ItemStack subtractOneAmount(@ValidItem ItemStack itemStack) {
        if (!isValid(itemStack)) {
            throw new RuntimeException("物品不合法");
        }

        int amount = itemStack.getAmount();

        if (amount == 1) {
            return null;
        }

        ItemStack newItemStack = itemStack.clone();

        newItemStack.setAmount(amount - 1);
        return newItemStack;
    }

    /**
     * 设置物品lore
     * @param itemStack 合法物品
     * @param lores
     * @return
     */
    public static ItemStack setLores(@ValidItem ItemStack itemStack, @Nullable List<String> lores) {
        if (!isValid(itemStack)) {
            throw new RuntimeException("物品不合法");
        }

        if (lores != null && lores.contains(null)) {
            throw new NullPointerException("包含空元素");
        }

        ItemStack resultItemStack = itemStack.clone();
        ItemMeta resultItemStackMeta = resultItemStack.getItemMeta();

        resultItemStackMeta.setLore(lores);
        resultItemStack.setItemMeta(resultItemStackMeta);
        return resultItemStack;
    }

    /**
     * 添加lore
     * @param itemStack 合法物品
     * @param lore
     * @return
     */
    public static ItemStack addLore(@ValidItem ItemStack itemStack, @NotNull String lore) {
        if (!isValid(itemStack)) {
            throw new RuntimeException("物品不合法");
        }

        List<String> lores = getLores(itemStack);

        lores.add(lore);
        return setLores(itemStack, lores);
    }

    /**
     * 得到物品名
     * @param itemStack
     * @return
     */
    public static @Nullable String getDisplayName(ItemStack itemStack) {
        if (!isValid(itemStack)) {
            return null;
        }

        return itemStack.getItemMeta().getDisplayName();
    }

    /**
     * 设置展示名
     * @param itemStack
     * @param displayName
     * @return
     */
    public static ItemStack setDisplayName(@ValidItem ItemStack itemStack, @NotNull String displayName) {
        if (!isValid(itemStack)) {
            throw new RuntimeException("物品不合法");
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
