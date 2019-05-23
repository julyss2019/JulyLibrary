package com.github.julyss2019.mcsp.julylibrary.inventory;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface InventoryListener {
    default void onClose(InventoryCloseEvent event) {}
    default void onOpen(InventoryOpenEvent event) {}
}
