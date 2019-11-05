package com.github.julyss2019.mcsp.julylibrary.item;

import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import com.github.julyss2019.mcsp.julylibrary.utils.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoreItemBuilder {
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private List<String> lores;
    private boolean colored;

    private LoreItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        List<String> tmp = itemMeta.getLore();
        this.lores = tmp == null ? new ArrayList<>() : tmp;
    }

    public LoreItemBuilder addLore(@Nullable String lore) {
        if (lore != null) {
            lores.add(lore);
        }

        return this;
    }

    public LoreItemBuilder insertLore(int index, @Nullable String lore) {
        if (lore != null) {
            lores.add(index, lore);
        }

        return this;
    }

    public LoreItemBuilder addLore(Collection<String> lores) {
        for (String lore : lores) {
            addLore(lore);
        }

        return this;
    }

    public LoreItemBuilder colored() {
        this.colored = true;
        return this;
    }

    public static LoreItemBuilder createNew(ItemStack itemStack) {
        if (!ItemUtil.isValidItem(itemStack)) {
            throw new RuntimeException("物品不能为空");
        }

        return new LoreItemBuilder(itemStack.clone());
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
