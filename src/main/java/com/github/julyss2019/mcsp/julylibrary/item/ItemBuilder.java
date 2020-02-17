package com.github.julyss2019.mcsp.julylibrary.item;


import com.github.julyss2019.mcsp.julylibrary.message.JulyText;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public class ItemBuilder {
    private Material material;
    private short durability;
    private int amount = 1;
    private String displayName;
    private Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    private ArrayList<String> lores = new ArrayList<>();
    private boolean colored = true;
    private Set<ItemFlag> itemFlags = new HashSet<>();
    private Map<String, String> placeholderMap = new HashMap<>();
    private String skullOwner;
    private String skullTexture;

    public ItemBuilder() {}

    @Deprecated
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

    @Deprecated
    public ItemBuilder data(short data) {
        this.durability = data;
        return this;
    }

    public ItemBuilder addItemFlags(@NotNull ItemFlag... itemFlags) {
        this.itemFlags.addAll(Arrays.asList(itemFlags));
        return this;
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
        this.material = Material.valueOf(name);
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
     * 设置lore
     * @param lores
     * @return
     */
    public ItemBuilder lores(@NotNull List<String> lores) {
        this.lores.clear();
        lores.forEach(this::addLore);
        return this;
    }

    /**
     * 设置lore
     * @param lores
     * @return
     */
    public ItemBuilder lores(@NotNull String... lores) {
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
    public ItemBuilder addLore(@NotNull String lore) {
        lores.add(lore);
        return this;
    }

    /**
     * 在指定位置插入lore
     * @param index
     * @param lore
     * @return
     */
    public ItemBuilder insertLore(int index, @NotNull String lore) {
        this.lores.add(index, lore);
        return this;
    }

    /**
     * 添加多个lore
     * @param lores
     * @return
     */
    public ItemBuilder addLores(@NotNull String... lores) {
        for (String lore : lores) {
            addLore(lore);
        }

        return this;
    }

    /**
     * 添加多个lore
     * @param lores
     * @return
     */
    public ItemBuilder addLores(@Nullable List<String> lores) {
        lores.forEach(this::addLore);
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

    /**
     * 颜色
     * @return
     */
    public ItemBuilder colored() {
        return colored(true);
    }

    /**
     * 颜色
     * @param colored
     * @return
     */
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
            throw new RuntimeException("物品不能为空");
        }

        if (this.amount <= 0) {
            throw new IllegalArgumentException("数量必须大于0");
        }

        ItemStack itemStack = new ItemStack(this.material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemStack.setAmount(this.amount);
        itemStack.setDurability(this.durability);

        if (itemMeta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;

            if (skullOwner != null) {
                skullMeta.setOwner(skullOwner);
            }

            if (skullTexture != null) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                Field profileField;

                profile.getProperties().put("textures", new Property("textures", this.skullTexture));

                try {
                    profileField = skullMeta.getClass().getDeclaredField("profile");

                    profileField.setAccessible(true);
                    profileField.set(skullMeta, profile);
                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
                    throw new RuntimeException(e1);
                }
            }

            itemMeta = skullMeta;
        } else {
            if (skullOwner != null) {
                throw new RuntimeException("要设置 skullOwner, 必须使 material 为 SKULL_ITEM");
            }

            if (skullTexture != null) {
                throw new RuntimeException("要设置 skullTexture, 必须使 material 为 SKULL_ITEM");
            }
        }

        if (lores != null) {
            itemMeta.setLore(this.colored ? JulyText.getColoredTexts(this.lores) : this.lores);
        }

        if (displayName != null) {
            itemMeta.setDisplayName(this.colored ? JulyText.getColoredText(this.displayName) : this.displayName);
        }

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

    public ItemBuilder skullOwner(String owner) {
        this.skullOwner = owner;
        return this;
    }

    public ItemBuilder skullTexture(String skullTexture) {
        this.skullTexture = skullTexture;
        return this;
    }
}
