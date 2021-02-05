package com.github.julyss2019.mcsp.julylibrary.item;


import com.github.julyss2019.mcsp.julylibrary.text.JulyText;
import com.github.julyss2019.mcsp.julylibrary.utils.NMSUtil;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ItemBuilder {
    private static Method ID_MATERIAL_METHOD;
    private static boolean ITEM_FLAG_ENABLED;
    private static boolean SKULL_TEXTURE_ENABLED;
    private Material material;
    private short durability;
    private int amount = 1;
    private String displayName;
    private Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    private ArrayList<String> lores = new ArrayList<>();
    private boolean colored = true;
    private Set<ItemFlag> itemFlags = new HashSet<>();
    private String skullOwner;
    private String skullTexture;

    static {
        try {
            ITEM_FLAG_ENABLED = Class.forName("org.bukkit.inventory.ItemFlag") != null;
        } catch (ClassNotFoundException ignored) {}

        try {
            ID_MATERIAL_METHOD = Material.class.getDeclaredMethod("getMaterial", int.class);
        } catch (NoSuchMethodException ignored) {}

        try {
            SKULL_TEXTURE_ENABLED = Class.forName("com.mojang.authlib.GameProfile") != null;
        } catch (ClassNotFoundException ignored) {}
    }

    public static boolean idMaterialEnabled() {
        return ID_MATERIAL_METHOD != null;
    }

    public static boolean isItemFlagEnabled() {
        return ITEM_FLAG_ENABLED;
    }

    public static boolean isSkullTextureEnabled() {
        return SKULL_TEXTURE_ENABLED;
    }

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

    @Deprecated
    public ItemBuilder addItemFlags(@NotNull ItemFlag... itemFlags) {
        for (ItemFlag itemFlag : itemFlags) {
            addItemFlag(itemFlag);
        }

        return this;
    }

    /**
     * 设置Flag
     * @param itemFlags
     * @return
     */
    public ItemBuilder itemFlags(@Nullable ItemFlag... itemFlags) {
        return itemFlags(new HashSet<>(Arrays.asList(itemFlags)));
    }

    /**
     * 设置Flag
     * @param itemFlags
     * @return
     */
    public ItemBuilder itemFlags(@Nullable Set<ItemFlag> itemFlags) {
        if (itemFlags == null) {
            this.itemFlags.clear();
            return this;
        }

        itemFlags.forEach(itemFlag -> {
            if (itemFlag == null) {
                throw new NullPointerException("存在 null 元素");
            }
        });

        this.itemFlags.clear();
        this.itemFlags.addAll(itemFlags);
        return this;
    }

    /**
     * 添加Flag
     * @param itemFlag
     * @return
     */
    public ItemBuilder addItemFlag(@NotNull ItemFlag itemFlag) {
        if (!ITEM_FLAG_ENABLED) {
            throw new RuntimeException("当前版本不支持 ItemFlag");
        }

        itemFlags.add(itemFlag);
        return this;
    }

    /**
     * 设置类型
     * @param material 材质
     * @return
     */
    public ItemBuilder material(@NotNull Material material) {
        if (material == Material.AIR) {
            throw new RuntimeException("Material 不允许为 AIR");
        }

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
    @Deprecated
    public ItemBuilder material(int id) {
        if (ID_MATERIAL_METHOD == null) {
            throw new RuntimeException("当前版本不支持数字ID: " + NMSUtil.NMS_VERSION);
        }

        try {
            this.material = (Material) ID_MATERIAL_METHOD.invoke(null, id);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

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
    public ItemBuilder lores(@Nullable List<String> lores) {
        if (lores == null) {
            this.lores.clear();
            return this;
        }

        lores.forEach(s -> {
            if (s == null) {
                throw new NullPointerException("存在 null 元素");
            }
        });

        this.lores.clear();
        this.lores.addAll(lores);
        return this;
    }

    /**
     * 设置lore
     * @param lores
     * @return
     */
    public ItemBuilder lores(@Nullable String... lores) {
        return lores(Arrays.asList(lores));
    }

    /**
     * 添加lore
     * @param lore
     * @return
     */
    public ItemBuilder addLore(@NotNull String lore) {
        this.lores.add(lore);
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
    public ItemBuilder addLores(@NotNull List<String> lores) {
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
    @Deprecated
    public ItemBuilder enchant(@NotNull Enchantment enchantment, int level) {
        return enchantment(enchantment, level);
    }

    public ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level) {
        enchantment(enchantment, level);
        return this;
    }

    /**
     * 添加附魔
     * @param enchantment
     * @param level
     * @return
     */
    public ItemBuilder enchantment(@NotNull Enchantment enchantment, int level) {
        if (level <= 0) {
            throw new IllegalArgumentException("level 必须 > 0");
        }

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

    public ItemBuilder skullOwner(@Nullable String owner) {
        this.skullOwner = owner;
        return this;
    }

    public ItemBuilder skullTexture(@Nullable String skullTexture) {
        if (skullTexture != null && !SKULL_TEXTURE_ENABLED) {
            throw new RuntimeException("当前版本不支持 skullTexture: " + NMSUtil.NMS_VERSION);
        }

        this.skullTexture = skullTexture;
        return this;
    }

    /**
     * 构造
     * @return
     */
    public ItemStack build() {
        if (this.material == null) {
            throw new RuntimeException("material 不能为 null");
        }

        if (this.material == Material.AIR) {
            throw new RuntimeException("material 不能为 AIR");
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
                GameProfile profile = new GameProfile(UUID.randomUUID(), UUID.randomUUID().toString());
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
                throw new RuntimeException("非头颅物品无法设置 skullTexture");
            }

            if (skullTexture != null) {
                throw new RuntimeException("非头颅物品无法设置 SkullOwner");
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
}
