package com.github.julyss2019.mcsp.julylibrary.inventory;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * 用于唤醒各种定义的事件
 */
public class InventoryEventFirer implements Listener {
    private Map<Inventory, List<IndexListenerItem>> itemListenerMap = new HashMap<>(); // 物品监听器
    private Map<Inventory, InventoryListener> inventoryListenerMap = new HashMap<>(); // 背包监听器
    private List<Inventory> cancelInteractInventories = new ArrayList<>();

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        // 背包监听器或背包物品监听器
        if (cancelInteractInventories.contains(clickedInventory)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }

        // 背包监听器
        if (inventoryListenerMap.containsKey(clickedInventory)) {
            inventoryListenerMap.get(clickedInventory).onClick(event);
            inventoryListenerMap.get(clickedInventory).onClicked(event);
        }

        // 背包物品监听器
        if (itemListenerMap.containsKey(clickedInventory)) {
            for (IndexListenerItem indexListenerItem : itemListenerMap.get(clickedInventory)) {
                if (event.getSlot() == indexListenerItem.getIndex()) {
                    ItemListener itemListener = indexListenerItem.getItemListener();

                    itemListener.onClicked(event); // 调用事件
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();

        if (cancelInteractInventories.contains(inventory)) {
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

        // 从 map 删除
        itemListenerMap.remove(inventory);
        inventoryListenerMap.remove(inventory);
        cancelInteractInventories.remove(inventory);
    }

    void cancelInteractInventory(@NotNull Inventory inventory) {
        cancelInteractInventories.add(inventory);
    }

    /**
     * 监听物品
     * @param inventory
     * @param indexListenerItems
     */
    void listenInventoryItems(@NotNull Inventory inventory, @NotNull List<IndexListenerItem> indexListenerItems) {
        itemListenerMap.put(inventory, indexListenerItems);
    }

    /**
     * 监听背包
     * @param inventory
     * @param inventoryListener
     */
    void listenInventory(@NotNull Inventory inventory, @NotNull InventoryListener inventoryListener) {
        inventoryListenerMap.put(inventory, inventoryListener);
    }

    /**
     * 得到被监听的物品
     * @return
     */
    public List<IndexListenerItem> getItemListeners() {
        List<IndexListenerItem> resultList = new ArrayList<>();

        for (List<IndexListenerItem> indexListenerItems : itemListenerMap.values()) {
            resultList.addAll(indexListenerItems);

        }

        return resultList;
    }

    public Collection<InventoryListener> getInventoryListeners() {
        return inventoryListenerMap.values();
    }
}
