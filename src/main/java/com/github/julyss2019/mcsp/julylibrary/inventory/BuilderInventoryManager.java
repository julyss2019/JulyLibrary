package com.github.julyss2019.mcsp.julylibrary.inventory;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BuilderInventoryManager {
    private Map<Inventory, BuilderInventory> bukkitInvToBuilderInvMap = new HashMap<>();

    public void registerBuilderInventory(@NotNull BuilderInventory builderInventory) {
        bukkitInvToBuilderInvMap.put(builderInventory.getBukkitInventory(), builderInventory);
    }

    public void unregisterBuilderInventory(@NotNull Inventory inventory) {
        bukkitInvToBuilderInvMap.remove(inventory);
    }

    public BuilderInventory getBuilderInventory(@NotNull Inventory inventory) {
        return bukkitInvToBuilderInvMap.get(inventory);
    }

    public boolean hasBuilderInventory(@NotNull Inventory inventory) {
        return bukkitInvToBuilderInvMap.containsKey(inventory);
    }
}
