package com.github.julyss2019.mcsp.julylibrary.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class ClickedCallback {
    private InventoryClickEvent inventoryClickEvent;

    public InventoryClickEvent getInventoryClickEvent() {
        return inventoryClickEvent;
    }

    public void setInventoryClickEvent(InventoryClickEvent inventoryClickEvent) {
        this.inventoryClickEvent = inventoryClickEvent;
    }

    public abstract void onClicked(Player player);
}
