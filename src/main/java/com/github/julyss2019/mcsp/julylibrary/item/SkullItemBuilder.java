package com.github.julyss2019.mcsp.julylibrary.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

@Deprecated
public class SkullItemBuilder extends ItemBuilder {
    private String texture;
    private String owner;

    public SkullItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    public SkullItemBuilder() {
        super.material(Material.SKULL_ITEM);
        super.durability((short) 3);
    }

    /**
     * 设置主人
     * @param owner
     * @return
     */
    public SkullItemBuilder owner(String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * 设置贴图
     * @param texture
     * @return
     */
    public SkullItemBuilder texture(String texture) {
        this.texture = texture;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = super.build();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!(itemMeta instanceof SkullMeta)) {
            throw new SkullItemBuilderException("物品必须为头颅!");
        }

        SkullMeta skullMeta = (SkullMeta) itemMeta;

        skullMeta.setOwner(this.owner);

        if (this.texture != null) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            Field profileField;

            profile.getProperties().put("textures", new Property("textures", this.texture));

            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");

                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }

        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
}
