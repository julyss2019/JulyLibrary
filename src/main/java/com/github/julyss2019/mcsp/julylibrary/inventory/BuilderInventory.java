package com.github.julyss2019.mcsp.julylibrary.inventory;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BuilderInventory {
    public static class ListenerItem {
        private int index;
        private ItemListener itemListener;

        public ListenerItem(int index, @NotNull ItemListener itemListener) {
            this.index = index;
            this.itemListener = itemListener;
        }

        public int getIndex() {
            return index;
        }

        public ItemListener getItemListener() {
            return itemListener;
        }
    }

    private Inventory bukkitInventory;
    private InventoryListener inventoryListener;
    private Map<Integer, ListenerItem> indexToListenerItemMap = new HashMap<>();
    private Set<Integer> canInteractIndexes = new HashSet<>();

    public BuilderInventory(@NotNull Inventory bukkitInventory) {
        this.bukkitInventory = bukkitInventory;
    }

    public Set<Integer> getWhitelistInteractIndexes() {
        return new HashSet<>(canInteractIndexes);
    }

    public void setInventoryListener(@Nullable InventoryListener inventoryListener) {
        this.inventoryListener = inventoryListener;
    }

    public ListenerItem getListenerItem(int index) {
        return indexToListenerItemMap.get(index);
    }

    public void setListenerItems(@NotNull Set<ListenerItem> listenerItems) {
        if (listenerItems.contains(null)) {
            throw new NullPointerException();
        }

        indexToListenerItemMap.clear();
        listenerItems.forEach(listenerItem -> {
            indexToListenerItemMap.put(listenerItem.index, listenerItem);
        });
    }

    public void setCanInteractIndexes(@NotNull Set<Integer> canInteractIndexes) {
        if (canInteractIndexes.contains(null)) {
            throw new NullPointerException();
        }

        this.canInteractIndexes = canInteractIndexes;
    }

    public Inventory getBukkitInventory() {
        return bukkitInventory;
    }

    public InventoryListener getInventoryListener() {
        return inventoryListener;
    }

    public Set<ListenerItem> getListenerItems() {
        return new HashSet<>(indexToListenerItemMap.values());
    }

    public boolean hasInventoryListener() {
        return inventoryListener != null;
    }
}
