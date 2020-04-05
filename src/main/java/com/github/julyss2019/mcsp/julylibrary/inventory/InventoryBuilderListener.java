package com.github.julyss2019.mcsp.julylibrary.inventory;

import com.github.julyss2019.mcsp.julylibrary.event.ItemClickEvent;
import org.bukkit.Bukkit;
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
public class InventoryBuilderListener implements Listener {
    private Map<Inventory, List<Item>> itemListenerMap = new HashMap<>(); // 物品监听器
    private Map<Inventory, InventoryListener> inventoryListenerMap = new HashMap<>(); // 背包监听器
    private List<Inventory> inventories = new ArrayList<>();

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        int topInventorySize = topInventory.getSize();
        int rawSlot = event.getRawSlot(); // 标准位置。Shift 时，rawSlot 是鼠标指针所在的位置。
        boolean shift = event.isShiftClick();

        // 取消点击GUI里的东西，取消全局 Shift
        if (inventories.contains(topInventory) && (shift || rawSlot < topInventorySize)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }

        // 背包
        if (inventoryListenerMap.containsKey(topInventory) && rawSlot < topInventorySize) {
            inventoryListenerMap.get(topInventory).onClick(event);

            // onClick 执行完时，GUI可能已经关闭了
            if (inventoryListenerMap.get(topInventory) != null) {
                inventoryListenerMap.get(topInventory).onClicked(event);
            }
        }

        // 背包物品
        if (itemListenerMap.containsKey(topInventory) && rawSlot < topInventorySize) {
            for (Item item : itemListenerMap.get(topInventory)) {
                if (rawSlot == item.getIndex()) {
                    ItemListener itemListener = item.getItemListener();
                    ItemClickEvent itemClickEvent = new ItemClickEvent(item, event);

                    Bukkit.getPluginManager().callEvent(itemClickEvent);

                    if (itemClickEvent.isCancelled()) {
                        return;
                    }

                    itemListener.onClicked(event); // 调用老事件

                    // 这时可能inv已经被关闭了
                    if (itemListenerMap.containsKey(topInventory)) {
                        itemListener.onClick(event); // 调用新事件
                    }

                    return;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();

        if (inventories.contains(inventory)) {
            int guiSize = event.getView().getTopInventory().getSize(); // 顶部GUI的大小

            for (int slot : event.getRawSlots()) {
                // 如果在顶部GUI点击
                if (slot > 0 && slot < guiSize) {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    return;
                }
            }
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
        inventories.remove(inventory);
    }

    void addInventory(@NotNull Inventory inventory) {
        inventories.add(inventory);
    }

    /**
     * 监听物品
     * @param inventory
     * @param items
     */
    void listenInventoryItems(@NotNull Inventory inventory, @NotNull List<Item> items) {
        itemListenerMap.put(inventory, items);
    }

    /**
     * 监听背包
     * @param inventory
     * @param inventoryListener
     */
    void addInventoryListener(@NotNull Inventory inventory, @NotNull InventoryListener inventoryListener) {
        inventoryListenerMap.put(inventory, inventoryListener);
    }
}
