package com.github.julyss2019.mcsp.julylibrary;

import com.github.julyss2019.mcsp.julylibrary.chat.ChatEventCaller;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatInterceptorManager;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryEventCaller;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class JulyLibrary extends JavaPlugin implements Listener {
    private static JulyLibrary instance;
    private InventoryEventCaller inventoryEventCaller;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(this.inventoryEventCaller = new InventoryEventCaller(), this);
        Bukkit.getPluginManager().registerEvents(new ChatEventCaller(), this);
        getCommand("jl").setExecutor((cs, command, s, args) -> {
            if (args.length == 1 && args[0].equalsIgnoreCase("monitor")) {
                cs.sendMessage("item_listener: " + inventoryEventCaller.getItemListeners().size());
                cs.sendMessage("inventory_listener: " + inventoryEventCaller.getInventoryListeners().size());
                return true;
            }

            return false;
        });

        getLogger().info("插件初始化完毕.");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        ChatInterceptorManager.unregisterAll();
        HandlerList.unregisterAll((Plugin) instance);
        getLogger().info("插件被卸载.");
    }

    /**
     * 得到背包事件触发器
     * @return
     */
    public InventoryEventCaller getInventoryEventCaller() {
        return inventoryEventCaller;
    }

    public static JulyLibrary getInstance() {
        return instance;
    }
}
