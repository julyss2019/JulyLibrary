package com.github.julyss2019.mcsp.julylibrary.inventory;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import com.github.julyss2019.mcsp.julylibrary.event.ItemClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

import java.util.Set;

/**
 * 用于唤醒各种定义的事件
 */
public class BuilderInventoryListener implements Listener {
    private BuilderInventoryManager builderInventoryManager = JulyLibrary.getInstance().getBuilderInventoryManager();

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        BuilderInventory builderInventory = builderInventoryManager.getBuilderInventories(topInventory);

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

        // 点击了 GUI 里的东西
        if (rawSlot < topInventorySize) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

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
            }

            // 背包监听器(最后触发)
            if (builderInventory.hasInventoryListener()) {
                builderInventory.getInventoryListener().onClick(event);
                builderInventory.getInventoryListener().onClicked(event);
            }
        }
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        BuilderInventory builderInventory = builderInventoryManager.getBuilderInventories(topInventory);

        if (builderInventory == null) {
            return;
        }

        int topInvSize = event.getView().getTopInventory().getSize(); // 顶部GUI的大小
        Set<Integer> rawSlots = event.getRawSlots();

/*        // 单击时可能触发 Drag ，这里单独判断提升用户体验
        if (rawSlots.size() == 1) {
            int v = rawSlots.iterator().next();

            if (v >= 0 && v < topInvSize) {
                return;
            } else {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }*/

        for (int slot : event.getRawSlots()) {
            // 如果在顶部GUI点击 且 是监听器物品 或 不是可单机物品
            // && (builderInventory.getListenerItem(slot) != null || !builderInventory.getWhitelistInteractIndexes().contains(slot))
            if (slot > 0 && slot < topInvSize) {
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
        Inventory inventory = event.getView().getTopInventory();
        BuilderInventory builderInventory = builderInventoryManager.getBuilderInventories(inventory);

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
        Inventory inventory = event.getView().getTopInventory();
        BuilderInventory builderInventory = builderInventoryManager.getBuilderInventories(inventory);

        if (builderInventory == null) {
            return;
        }

        if (builderInventory.hasInventoryListener()) {
            builderInventory.getInventoryListener().onClose(event);
        }

        builderInventoryManager.unregisterBuilderInventory(inventory);

/*        new BukkitRunnable() {
            @Override
            public void run() {
                // 如果没人看了 注销
                if (inventory.getViewers().size() == 0) {
                    builderInventoryManager.unregisterBuilderInventory(inventory);
                }
            }
        }.runTask(JulyLibrary.getInstance());*/
    }
}
