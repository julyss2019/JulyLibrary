package com.github.julyss2019.mcsp.julylibrary.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ItemListener {
    default void onClicked(InventoryClickEvent event) {}
}
