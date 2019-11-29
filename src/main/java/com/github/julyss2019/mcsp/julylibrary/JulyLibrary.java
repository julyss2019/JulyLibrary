package com.github.julyss2019.mcsp.julylibrary;

import com.github.julyss2019.mcsp.julylibrary.chat.ChatEventFirer;
import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatInterceptor;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryEventFirer;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class JulyLibrary extends JavaPlugin implements Listener {
    private static JulyLibrary instance;
    private InventoryEventFirer inventoryEventFirer;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(this.inventoryEventFirer = new InventoryEventFirer(), this);
        Bukkit.getPluginManager().registerEvents(new ChatEventFirer(), this);
        getCommand("jl").setExecutor((cs, command, s, args) -> {
            if (args.length == 1 && args[0].equalsIgnoreCase("monitor")) {
                cs.sendMessage("item_listener: " + inventoryEventFirer.getItemListeners().size());
                cs.sendMessage("inventory_listener: " + inventoryEventFirer.getInventoryListeners().size());
                return true;
            }

            return false;
        });

        getLogger().info("插件初始化完毕.");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        JulyChatInterceptor.unregisterAll();
        HandlerList.unregisterAll((Plugin) instance);
        getLogger().info("插件被卸载.");
    }

    /**
     * 得到背包事件触发器
     * @return
     */
    public InventoryEventFirer getInventoryEventFirer() {
        return inventoryEventFirer;
    }

    public static JulyLibrary getInstance() {
        return instance;
    }
}
