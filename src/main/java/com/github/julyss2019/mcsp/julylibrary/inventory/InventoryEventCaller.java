package com.github.julyss2019.mcsp.julylibrary.inventory;

import com.github.julyss2019.mcsp.julylibrary.event.ItemClickEvent;
import com.github.julyss2019.mcsp.julylibrary.utils.NMSUtil;
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
public class InventoryEventCaller implements Listener {
    private Map<Inventory, List<Item>> itemListenerMap = new HashMap<>(); // 物品监听器
    private Map<Inventory, InventoryListener> inventoryListenerMap = new HashMap<>(); // 背包监听器
    private List<Inventory> cancelInteractInventories = new ArrayList<>();

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory clickedInventory;

        if (NMSUtil.NMS_VERSION.equalsIgnoreCase("v1_7_R4")) {
            clickedInventory = event.getInventory();
        } else {
            clickedInventory = event.getClickedInventory();
        }

        // 背包监听器或背包物品监听器
        if (cancelInteractInventories.contains(clickedInventory)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }

        // 背包监听器
        if (inventoryListenerMap.containsKey(clickedInventory)) {
            inventoryListenerMap.get(clickedInventory).onClick(event);

            // onClick 执行完时，GUI可能已经关闭了
            if (inventoryListenerMap.get(clickedInventory) != null) {
                inventoryListenerMap.get(clickedInventory).onClicked(event);
            }
        }

        // 背包物品监听器
        if (itemListenerMap.containsKey(clickedInventory)) {
            for (Item item : itemListenerMap.get(clickedInventory)) {
                if (event.getSlot() == item.getIndex()) {
                    ItemListener itemListener = item.getItemListener();
                    ItemClickEvent itemClickEvent = new ItemClickEvent(item, event);

                    Bukkit.getPluginManager().callEvent(itemClickEvent);

                    if (itemClickEvent.isCancelled()) {
                        return;
                    }

                    itemListener.onClicked(event); // 调用老事件

                    // 这时可能inv已经被关闭了
                    if (itemListenerMap.containsKey(clickedInventory)) {
                        itemListener.onClick(event); // 调用新事件
                    }

                    return;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();

        if (cancelInteractInventories.contains(inventory)) {
            int guiSize = event.getView().getTopInventory().getSize();

            for (int slot : event.getRawSlots()) {
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
        cancelInteractInventories.remove(inventory);
    }

    void addCancelIntercatInventory(@NotNull Inventory inventory) {
        cancelInteractInventories.add(inventory);
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

    /**
     * 得到被监听的物品
     * @return
     */
    public List<Item> getItemListeners() {
        List<Item> resultList = new ArrayList<>();

        for (List<Item> items : itemListenerMap.values()) {
            resultList.addAll(items);

        }

        return resultList;
    }

    public Collection<InventoryListener> getInventoryListeners() {
        return inventoryListenerMap.values();
    }
}
