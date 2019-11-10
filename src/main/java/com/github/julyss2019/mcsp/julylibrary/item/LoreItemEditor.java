package com.github.julyss2019.mcsp.julylibrary.item;

import com.github.julyss2019.mcsp.julylibrary.Matcher;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import com.github.julyss2019.mcsp.julylibrary.utils.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoreItemEditor {
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private List<String> lores;
    private boolean colored;

    private LoreItemEditor(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        List<String> tmp = itemMeta.getLore();
        this.lores = tmp == null ? new ArrayList<>() : tmp;
    }

    /**
     * 根据匹配器删除lore
     * @param matcher
     * @return
     */
    public LoreItemEditor remove(Matcher<String> matcher) {
        List<String> resultList = new ArrayList<>();

        if (lores == null) {
            return this;
        }

        for (String lore : lores) {
            if (!matcher.match(lore)) {
                resultList.add(lore);
            }
        }

        this.lores = resultList;
        return this;
    }

    public LoreItemEditor addLore(@Nullable String lore) {
        if (lore != null) {
            lores.add(lore);
        }

        return this;
    }

    public LoreItemEditor insertLore(int index, @Nullable String lore) {
        if (lore != null) {
            lores.add(index, lore);
        }

        return this;
    }

    public LoreItemEditor addLore(Collection<String> lores) {
        for (String lore : lores) {
            addLore(lore);
        }

        return this;
    }

    public LoreItemEditor colored() {
        this.colored = true;
        return this;
    }

    public static LoreItemEditor createNew(ItemStack itemStack) {
        if (!ItemUtil.isValidItem(itemStack)) {
            throw new RuntimeException("物品不能为空");
        }

        return new LoreItemEditor(itemStack.clone());
    }

    public ItemStack build() {
        if (colored) {
            List<String> coloredLores = new ArrayList<>();

            for (String lore : lores) {
                coloredLores.add(JulyMessage.toColoredMessage(lore));
            }

            itemMeta.setLore(coloredLores);
        } else {
            itemMeta.setLore(lores);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
