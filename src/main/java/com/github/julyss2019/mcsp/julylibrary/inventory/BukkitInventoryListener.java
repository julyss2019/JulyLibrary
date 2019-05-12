package com.github.julyss2019.mcsp.julylibrary.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukkitInventoryListener implements Listener {
    private Map<Inventory, List<ListenerItem>> itemListenerMap = new HashMap<>(); // 物品监听器
    private Map<Inventory, InventoryListener> inventoryListenerMap = new HashMap<>(); // 背包监听器

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        if (itemListenerMap.containsKey(clickedInventory)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

            for (ListenerItem listenerItem : itemListenerMap.get(clickedInventory)) {
                if (event.getSlot() == listenerItem.getIndex()) {
                    ItemListener itemListener = listenerItem.getItemListener();

                    itemListener.onClicked(event); // 调用事件
                }
            }
        }
    }

    /**
     * 用于触发背包监听器的 onClose()
     * @param event
     */
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if (inventoryListenerMap.containsKey(inventory)) {
            inventoryListenerMap.get(inventory).onClose(event);
        }

        itemListenerMap.remove(inventory); // 释放空间
    }

    protected void registerListenerItems(@NotNull Inventory inventory, @NotNull List<ListenerItem> listenerItems) {
        itemListenerMap.put(inventory, listenerItems);
    }

    protected void registerInventoryListeners(@NotNull Inventory inventory, @NotNull InventoryListener inventoryListener) {
        inventoryListenerMap.put(inventory, inventoryListener);
    }
}
