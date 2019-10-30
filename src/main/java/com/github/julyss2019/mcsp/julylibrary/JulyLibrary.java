package com.github.julyss2019.mcsp.julylibrary;

import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryEventFirer;
import com.github.julyss2019.mcsp.julylibrary.logger.JulyFileLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class JulyLibrary extends JavaPlugin {
    private static JulyLibrary instance;
    private InventoryEventFirer inventoryEventFirer;

    @Override
    public void onEnable() {
        instance = this;
        this.inventoryEventFirer = new InventoryEventFirer();

        JulyChatFilter.init();
        JulyFileLogger.init();
        Bukkit.getPluginManager().registerEvents(new InventoryEventFirer(), this);
        getLogger().info("插件初始化完毕!");

        getCommand("jl").setExecutor((cs, command, s, args) -> {
            if (args.length == 1 && args[0].equalsIgnoreCase("monitor")) {
                cs.sendMessage("item_listener: " + inventoryEventFirer.getItemListeners().size());
                cs.sendMessage("inventory_listener: " + inventoryEventFirer.getInventoryListeners().size());
                return true;
            }

            return false;
        });
    }

    @Override
    public void onDisable() {
        JulyFileLogger.closeLoggers();
        JulyChatFilter.unregisterAll();
        Bukkit.getScheduler().cancelTasks(this);
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
