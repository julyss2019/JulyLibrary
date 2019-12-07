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
import java.util.Optional;

public class LoreItemEditor {
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private List<String> lores;
    private boolean colored;

    public LoreItemEditor(ItemStack itemStack) {
        if (!ItemUtil.isValidItem(itemStack)) {
            throw new RuntimeException("物品不能为空");
        }

        this.itemStack = itemStack.clone();
        this.itemMeta = this.itemStack.getItemMeta();
        this.lores = Optional.ofNullable(itemMeta.getLore()).orElse(new ArrayList<>());
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
