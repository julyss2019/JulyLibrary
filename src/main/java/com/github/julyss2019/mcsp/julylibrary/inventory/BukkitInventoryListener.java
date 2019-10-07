package com.github.julyss2019.mcsp.julylibrary.inventory;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于唤醒各种定义的事件
 */
public class BukkitInventoryListener implements Listener {
    private Map<Inventory, List<ListenerItem>> itemListenerMap = new HashMap<>(); // 物品监听器
    private Map<Inventory, InventoryListener> inventoryListenerMap = new HashMap<>(); // 背包监听器
    private List<Inventory> cancelClickInventories = new ArrayList<>();

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Inventory clickedInventory = event.getClickedInventory();

        if (cancelClickInventories.contains(clickedInventory) || itemListenerMap.containsKey(clickedInventory) || cancelClickInventories.contains(inventory) || itemListenerMap.containsKey(inventory)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

        }

        if (inventoryListenerMap.containsKey(clickedInventory)) {
            inventoryListenerMap.get(clickedInventory).onClicked(event);
        }

        if (itemListenerMap.containsKey(clickedInventory)) {

            for (ListenerItem listenerItem : itemListenerMap.get(clickedInventory)) {
                if (event.getSlot() == listenerItem.getIndex()) {
                    ItemListener itemListener = listenerItem.getItemListener();

                    itemListener.onClicked(event); // 调用事件
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();

        if (cancelClickInventories.contains(inventory) || itemListenerMap.containsKey(inventory)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    /**
     * 用于触发背包监听器的 onOpen()
     * @param event
     */
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();

        if (inventoryListenerMap.containsKey(inventory)) {
            inventoryListenerMap.get(inventory).onOpen(event);
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

        // 释放空间
        itemListenerMap.remove(inventory);
        inventoryListenerMap.remove(inventory);
        cancelClickInventories.remove(inventory);
    }

    void registerListenerItems(@NotNull Inventory inventory, @NotNull List<ListenerItem> listenerItems) {
        itemListenerMap.put(inventory, listenerItems);
    }

    void registerInventoryListener(@NotNull Inventory inventory, @NotNull InventoryListener inventoryListener) {
        inventoryListenerMap.put(inventory, inventoryListener);
    }

    void registerCancelClickInventory(@NotNull Inventory inventory) {
        cancelClickInventories.add(inventory);
    }

    public Map<Inventory, List<ListenerItem>> getItemListenerMap() {
        return itemListenerMap;
    }

    public Map<Inventory, InventoryListener> getInventoryListenerMap() {
        return inventoryListenerMap;
    }
}
