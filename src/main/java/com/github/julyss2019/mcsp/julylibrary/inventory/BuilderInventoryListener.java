package com.github.julyss2019.mcsp.julylibrary.inventory;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import com.github.julyss2019.mcsp.julylibrary.event.ItemClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

/**
 * 用于唤醒各种定义的事件
 */
public class BuilderInventoryListener implements Listener {
    private BuilderInventoryManager builderInventoryManager = JulyLibrary.getInstance().getBuilderInventoryManager();

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        BuilderInventory builderInventory = builderInventoryManager.getBuilderInventory(topInventory);

        if (builderInventory == null) {
            return;
        }

        InventoryAction action = event.getAction();

        // 取消 Shift，过滤非常规操作
        if (event.isShiftClick() || action == InventoryAction.COLLECT_TO_CURSOR) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
            return;
        }

        int topInventorySize = topInventory.getSize();
        int rawSlot = event.getRawSlot();

        // 取消点击GUI里的东西
        if (rawSlot < topInventorySize) {
            // 背包监听器
            if (builderInventory.hasInventoryListener()) {
                builderInventory.getInventoryListener().onClick(event);
                builderInventory.getInventoryListener().onClicked(event);
            }

            BuilderInventory.ListenerItem listenerItem = builderInventory.getListenerItem(rawSlot);

            if (listenerItem != null) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);

                ItemClickEvent itemClickEvent = new ItemClickEvent(listenerItem, event);

                Bukkit.getPluginManager().callEvent(itemClickEvent);

                if (!itemClickEvent.isCancelled()) {
                    listenerItem.getItemListener().onClick(event);
                    listenerItem.getItemListener().onClicked(event);
                }
                // 白名单位置
            } else if (!builderInventory.getWhitelistInteractIndexes().contains(rawSlot)) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        BuilderInventory builderInventory = builderInventoryManager.getBuilderInventory(inventory);

        if (builderInventory == null) {
            return;
        }

        int topInvSize = event.getView().getTopInventory().getSize(); // 顶部GUI的大小

        for (int slot : event.getRawSlots()) {
            // 如果在顶部GUI点击 且 是监听器物品 或 不是可单机物品
            if (slot > 0 && slot < topInvSize && (builderInventory.getListenerItem(slot) != null || !builderInventory.getWhitelistInteractIndexes().contains(slot))) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                return;
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
        BuilderInventory builderInventory = builderInventoryManager.getBuilderInventory(inventory);

        if (builderInventory == null) {
            return;
        }

        if (builderInventory.hasInventoryListener()) {
            builderInventory.getInventoryListener().onOpen(event);
        }
    }

    /**
     * 用于触发背包监听器的 onClose()
     * @param event
     */
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        BuilderInventory builderInventory = builderInventoryManager.getBuilderInventory(inventory);

        if (builderInventory == null) {
            return;
        }

        if (builderInventory.hasInventoryListener()) {
            builderInventory.getInventoryListener().onClose(event);
        }
    }
}
