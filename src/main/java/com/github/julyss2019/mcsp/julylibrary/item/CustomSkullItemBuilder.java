package com.github.julyss2019.mcsp.julylibrary.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class CustomSkullItemBuilder extends ItemBuilder {
    private String texture = null;

    public CustomSkullItemBuilder() {}

    public CustomSkullItemBuilder(ItemStack itemStack) {
        super(itemStack);
    }

    /**
     * 设置贴图
     * @param texture
     * @return
     */
    public CustomSkullItemBuilder texture(String texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public ItemStack build() {
        ItemStack itemStack = super.build();
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        if (itemStack.getDurability() != 3) {
            throw new RuntimeException("durability 必须为 3.");
        }

        if (texture != null) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            Field profileField;

            if (texture != null) {
                profile.getProperties().put("textures", new Property("textures", texture));
            }

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
