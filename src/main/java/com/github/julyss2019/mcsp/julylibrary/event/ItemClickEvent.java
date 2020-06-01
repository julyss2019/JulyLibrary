package com.github.julyss2019.mcsp.julylibrary.event;

import com.github.julyss2019.mcsp.julylibrary.inventory.BuilderInventory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class ItemClickEvent extends Event implements Cancellable {
    private static HandlerList handlerList = new HandlerList();
    private boolean cancelled;
    private BuilderInventory.ListenerItem clicked;
    private InventoryClickEvent inventoryClickEvent;

    public ItemClickEvent(@NotNull BuilderInventory.ListenerItem listenerItem, @NotNull InventoryClickEvent inventoryClickEvent) {
        this.clicked = clicked;
        this.inventoryClickEvent = inventoryClickEvent;
    }

    public BuilderInventory.ListenerItem getClicked() {
        return clicked;
    }

    public InventoryClickEvent getInventoryClickEvent() {
        return inventoryClickEvent;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
