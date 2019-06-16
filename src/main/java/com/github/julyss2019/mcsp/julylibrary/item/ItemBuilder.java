package com.github.julyss2019.mcsp.julylibrary.item;


import com.github.julyss2019.mcsp.julylibrary.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemBuilder {
    private Material material;
    private short durability;
    private int amount = 1;
    private String displayName;
    private Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    private List<String> lores = new ArrayList<>();
    private int loreCounter = 0;
    private boolean colored;
    private List<ItemFlag> itemFlags = new ArrayList<>();

    public ItemBuilder(ItemBuilder itemBuilder) {
        this.material = itemBuilder.material;
        this.durability = itemBuilder.durability;
    }

    public ItemBuilder() {}

    public ItemBuilder(ItemStack itemStack) {
        this.material = itemStack.getType();
        this.durability = itemStack.getDurability();
        this.amount = itemStack.getAmount();
        this.displayName = itemStack.getItemMeta().getDisplayName();

        if (itemStack.getItemMeta().getLore() != null) {
            lores.addAll(itemStack.getItemMeta().getLore());
        }

        if (itemStack.getEnchantments() != null) {
            enchantmentMap.putAll(itemStack.getEnchantments());
        }
    }

    public ItemBuilder addItemFlag(@NotNull ItemFlag itemFlag) {
        itemFlags.add(itemFlag);
        return this;
    }

    /**
     * 设置类型
     * @param material 材质
     * @return
     */
    public ItemBuilder material(@NotNull Material material) {
        this.material = material;
        return this;
    }

    /**
     * 设置类型
     * @param name 物品名
     * @return
     */
    public ItemBuilder material(@NotNull String name) {
        this.material = Material.getMaterial(name);
        return this;
    }

    /**
     * 设置类型
     * @param id 物品id
     * @return
     */
    public ItemBuilder material(int id) {
        this.material = Material.getMaterial(id);
        return this;
    }

    /**
     * 设置子ID
     * @param durability
     * @return
     */
    public ItemBuilder durability(short durability) {
        this.durability = durability;
        return this;
    }

    /**
     * 在最前面递增添加lore
     * @param lore
     * @return
     */
    public ItemBuilder insertBeforeLore(@Nullable String lore) {
        if (lore != null) {
            this.lores.add(loreCounter++, lore);
        }

        return this;
    }

    /**
     * 设置lore
     * @param lores
     * @return
     */
    public ItemBuilder lores(@Nullable List<String> lores) {
        this.lores.clear();

        if (lores != null) {
            this.lores.addAll(lores);
        }

        return this;
    }

    /**
     * 设置lore
     * @param lores
     * @return
     */
    public ItemBuilder lores(@Nullable String... lores) {
        this.lores.clear();

        if (lores != null) {
            this.lores.addAll(Arrays.asList(lores));
        }

        return this;
    }

    /**
     * 添加lore
     * @param lore
     * @return
     */
    public ItemBuilder addLore(@Nullable String lore) {
        if (lore != null) {
            lores.add(lore);
        }

        return this;
    }

    /**
     * 在指定位置插入lore
     * @param index
     * @param lore
     * @return
     */
    public ItemBuilder insertLore(int index, @Nullable String lore) {
        if (lore != null) {
            this.lores.add(index, lore);
        }

        return this;
    }

    /**
     * 添加多个lore
     * @param lores
     * @return
     */
    public ItemBuilder addLores(@NotNull String... lores) {
        this.lores.addAll(Arrays.asList(lores));
        return this;
    }

    /**
     * 添加多个lore
     * @param lores
     * @return
     */
    public ItemBuilder addLores(@NotNull List<String> lores) {
        this.lores.addAll(lores);
        return this;
    }

    /**
     * 设置物品名
     * @param displayName
     * @return
     */
    public ItemBuilder displayName(@Nullable String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * 设置附魔
     * @param enchantment
     * @param level
     * @return
     */
    public ItemBuilder enchant(@NotNull Enchantment enchantment, int level) {
        this.enchantmentMap.put(enchantment, level);
        return this;
    }

    /**
     * 设置数量
     * @param amount
     * @return
     */
    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder colored() {
        return colored(true);
    }

    public ItemBuilder colored(boolean colored) {
        this.colored = colored;
        return this;
    }

    /**
     * 构造
     * @return
     */
    public ItemStack build() {
        if (this.material == null || this.material == Material.AIR) {
            throw new ItemBuilderException("物品不能为空");
        }

        ItemStack itemStack = new ItemStack(this.material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemStack.setAmount(this.amount);
        itemStack.setDurability(this.durability);
        itemMeta.setLore(this.colored ? MessageUtil.translateColorCode(this.lores) : this.lores);
        itemMeta.setDisplayName(this.colored ? MessageUtil.translateColorCode(this.displayName) : this.displayName);

        for (ItemFlag itemFlag : itemFlags) {
            itemMeta.addItemFlags(itemFlag);
        }

        for (Map.Entry<Enchantment, Integer> entry : this.enchantmentMap.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int lv = entry.getValue();

            if (lv == 0) {
                itemStack.removeEnchantment(enchantment);
                continue;
            }

            itemMeta.addEnchant(enchantment, lv, true);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
