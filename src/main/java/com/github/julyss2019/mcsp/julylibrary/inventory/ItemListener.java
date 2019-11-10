package com.github.julyss2019.mcsp.julylibrary.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ItemListener {
    @Deprecated
    default void onClicked(InventoryClickEvent event) {}

    default void onClick(InventoryClickEvent event) {}
}
