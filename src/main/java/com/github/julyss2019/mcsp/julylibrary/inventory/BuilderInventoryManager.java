package com.github.julyss2019.mcsp.julylibrary.inventory;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BuilderInventoryManager {
    private Map<Inventory, BuilderInventory> inventoryToBuilderInventoryHashMap = new HashMap<>();

    public void registerBuilderInventory(@NotNull BuilderInventory builderInventory) {
        inventoryToBuilderInventoryHashMap.put(builderInventory.getBukkitInventory(), builderInventory);
    }

    public void unregisterBuilderInventory(@NotNull Inventory inventory) {
        inventoryToBuilderInventoryHashMap.remove(inventory);
    }

    public BuilderInventory getBuilderInventories(@NotNull Inventory inventory) {
        return inventoryToBuilderInventoryHashMap.get(inventory);
    }

    public boolean hasBuilderInventory(@NotNull Inventory inventory) {
        return inventoryToBuilderInventoryHashMap.containsKey(inventory);
    }

    public Set<BuilderInventory> getBuilderInventories() {
        return new HashSet<>(inventoryToBuilderInventoryHashMap.values());
    }
}
