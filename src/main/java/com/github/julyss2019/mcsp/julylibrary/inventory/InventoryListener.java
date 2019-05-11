package com.github.julyss2019.mcsp.julylibrary.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryListener implements Listener {
    private Map<Inventory, List<ClickableItem>> itemMap = new HashMap<>();

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();

        if (itemMap.containsKey(clickedInventory)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

            for (ClickableItem clickableItem : itemMap.get(clickedInventory)) {
                if (event.getSlot() == clickableItem.getIndex()) {
                    ClickedCallback clickedCallback = clickableItem.getClickedCallback();

                    clickedCallback.onClicked(player); // 调用回调
                    clickedCallback.setInventoryClickEvent(event);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        itemMap.remove(event.getInventory()); // 释放空间
    }

    protected void registerItems(Inventory inventory, List<ClickableItem> clickableItems) {
        itemMap.put(inventory, clickableItems);
    }
}
