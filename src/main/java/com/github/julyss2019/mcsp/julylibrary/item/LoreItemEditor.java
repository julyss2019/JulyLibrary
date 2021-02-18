package com.github.julyss2019.mcsp.julylibrary.item;

import com.github.julyss2019.mcsp.julylibrary.Matcher;
import com.github.julyss2019.mcsp.julylibrary.text.JulyText;
import com.github.julyss2019.mcsp.julylibrary.utils.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LoreItemEditor {
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private List<String> lores;
    private boolean colored = true;

    public LoreItemEditor(ItemStack itemStack) {
        if (!ItemUtil.isValidItem(itemStack)) {
            throw new RuntimeException("物品不能为空");
        }

        this.itemStack = itemStack.clone();
        this.itemMeta = this.itemStack.getItemMeta();
        this.lores = Optional.ofNullable(itemMeta.getLore()).orElse(new ArrayList<>());
    }

    public LoreItemEditor replaceLore(@NotNull String oldStr, @NotNull String newStr) {
        this.lores = lores.stream().map(s -> {
           return s.replace(oldStr, newStr);
        }).collect(Collectors.toList());

        return this;
    }

    public LoreItemEditor clearLores() {
        lores.clear();
        return this;
    }

    /**
     * 根据匹配器删除lore
     * @param matcher
     * @return
     */
    @Deprecated
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

    /**
     * 删除lore
     * @param predicate 条件
     * @return
     */
    public LoreItemEditor removeLore(@NotNull Predicate<String> predicate) {
        lores.removeIf(predicate);
        return this;
    }

    /**
     * 删除lore
     * @param index 索引
     * @return
     */
    public LoreItemEditor removeLore(int index) {
        lores.remove(index);
        return this;
    }

    /**
     * 添加lore
     * @param lore
     * @return
     */
    public LoreItemEditor addLore(@NotNull String lore) {
        lores.add(lore);
        return this;
    }

    /**
     * 插入lore
     * @param index
     * @param lore
     * @return
     */
    public LoreItemEditor insertLore(int index, @Nullable String lore) {
        if (lore != null) {
            lores.add(index, lore);
        }

        return this;
    }

    /**
     * 添加lore
     * @param lores
     * @return
     */
    public LoreItemEditor addLores(@NotNull List<String> lores) {
        lores.forEach(this::addLore);
        return this;
    }

    /**
     * 设置lores
     * @param lores
     * @return
     */
    public LoreItemEditor setLores(@NotNull List<String> lores) {
        clearLores();
        addLores(lores);
        return this;
    }

    public List<String> getLores() {
        return new ArrayList<>(lores);
    }

    /**
     * 着色
     * @return
     */
    public LoreItemEditor colored() {
        this.colored = true;
        return this;
    }

    @Deprecated
    public ItemStack build() {
        return get();
    }

    public ItemStack get() {
        if (colored) {
            itemMeta.setLore(lores.stream().map(JulyText::getColoredText).collect(Collectors.toList()));
        } else {
            itemMeta.setLore(lores);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
