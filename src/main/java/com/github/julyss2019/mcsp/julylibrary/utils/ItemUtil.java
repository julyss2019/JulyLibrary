package com.github.julyss2019.mcsp.julylibrary.utils;

import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    /**
     * 得到物品ID（包含子ID）
     * @param itemStack
     * @return
     */
    public static @Nullable String getItemId(@NotNull ItemStack itemStack) {
        if (!isValidItem(itemStack)) {
            throw new RuntimeException("物品不合法");
        }

        int id = itemStack.getTypeId();
        short data = itemStack.getDurability();

        return id + (data == 0 ? "" : ":" + data);
    }

    /**
     * 判断物品是否有效（不为null且meta不为null）
     * @param itemStack
     * @return
     */
    public static boolean isValidItem(@Nullable ItemStack itemStack) {
        return itemStack != null && itemStack.getItemMeta() != null;
    }

    /**
     * 判断物品是否包含指定lore
     * @param itemStack
     * @param lore
     * @return
     */
    public static boolean containsLore(@Nullable ItemStack itemStack, @NotNull String lore) {
        if (!isValidItem(itemStack)) {
            return false;
        }

        List<String> lores = itemStack.getItemMeta().getLore();

        return lores != null && lores.contains(JulyMessage.toColoredMessage(lore));
    }

    /**
     * 得到物品lore
     * @param itemStack
     * @return 不为空的`List
     */
    public static List<String> getLores(@Nullable ItemStack itemStack) {
        if (!isValidItem(itemStack)) {
            return new ArrayList<>();
        }

        List<String> lores = itemStack.getItemMeta().getLore();

        return lores == null ? new ArrayList<>() : lores;
    }

    /**
     * 通过配置节点得到物品
     * @param section
     * @return
     */
    public static ItemBuilder getItemBuilderBySection(@NotNull ConfigurationSection section) {
        ItemBuilder itemBuilder = new ItemBuilder()
                .colored()
                .displayName(section.getString("display_name"))
                .lores(section.getStringList("lores"));

        if (section.contains("id")) {
            itemBuilder.material(section.getInt("id"));
        } else if (section.contains("material")) {
            itemBuilder.material(section.getString("material"));
        } else {
            throw new RuntimeException("ConfigurationSection material或id 未指定");
        }

        if (section.contains("data")) {
            itemBuilder.data((short) section.getInt("data"));
        }

        return itemBuilder;
    }

    /**
     * 减去物品的一个数量 如果-1后数量为0则返回null
     * @param itemStack
     * @return
     */
    public static ItemStack subtractOneAmount(@NotNull ItemStack itemStack) {
        if (!isValidItem(itemStack)) {
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

    @Deprecated
    public static ItemStack getItemByID(String idStr) {
        String[] aId = idStr.split(":");

        for (String id : aId) {
            if (id.matches("[0-9]+")) {
                throw new RuntimeException("非法的ID");
            }
        }

        return new ItemStack(Integer.parseInt(aId[0]), 1, aId.length == 1 ? 0 : Short.parseShort(aId[1]));
    }

    @Deprecated
    public static ItemStack addLore(ItemStack itemStack, String lore) {
        if (!isValidItem(itemStack)) {
            return null;
        }

        ItemStack resultItem = itemStack.clone();
        ItemMeta resultItemMeta = resultItem.getItemMeta();
        List<String> lores = resultItemMeta.getLore();

        lores.add(lore);
        resultItemMeta.setLore(lores);
        resultItem.setItemMeta(resultItemMeta);
        return resultItem;
    }

    @Deprecated
    public static ItemBuilder toItemBuilder(ItemStack itemStack) {
        ItemBuilder itemBuilder = new ItemBuilder();

        if (!ItemUtil.isValidItem(itemStack)) {
            return itemBuilder;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemBuilder.material(itemStack.getType());
        itemBuilder.data(itemStack.getDurability());
        itemBuilder.displayName(itemMeta.getDisplayName());
        itemBuilder.lores(itemMeta.getLore());
        return itemBuilder;
    }
}
