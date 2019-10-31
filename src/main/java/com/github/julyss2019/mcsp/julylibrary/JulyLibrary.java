package com.github.julyss2019.mcsp.julylibrary;

import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryEventFirer;
import com.github.julyss2019.mcsp.julylibrary.logger.FileLogger;
import com.github.julyss2019.mcsp.julylibrary.logger.JulyFileLogger;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class JulyLibrary extends JavaPlugin {
    private static JulyLibrary instance;
    private InventoryEventFirer inventoryEventFirer;
    private int tickCounter = 0;

    @Override
    public void onEnable() {
        instance = this;
        this.inventoryEventFirer = new InventoryEventFirer();

        Bukkit.getPluginManager().registerEvents(inventoryEventFirer, this);
        getCommand("jl").setExecutor((cs, command, s, args) -> {
            if (args.length == 1 && args[0].equalsIgnoreCase("monitor")) {
                cs.sendMessage("item_listener: " + inventoryEventFirer.getItemListeners().size());
                cs.sendMessage("inventory_listener: " + inventoryEventFirer.getInventoryListeners().size());
                return true;
            }

            return false;
        });

        JulyChatFilter.init();
        JulyMessage.init();
        // 定时 flush 任务
        new BukkitRunnable() {
            @Override
            public void run() {
                tickCounter++;

                // 遍历所有 FileLogger
                for (FileLogger fileLogger : JulyFileLogger.getLoggers()) {
                    int interval = fileLogger.getSaveInterval();

                    // 检查间隔
                    if (interval == 0 || tickCounter % interval == 0) {
                        fileLogger.flush();
                    }
                }
            }
        }.runTaskTimer(JulyLibrary.getInstance(), 0L, 20L);

        getLogger().info("插件初始化完毕1.");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        JulyFileLogger.closeLoggers();
        JulyChatFilter.unregisterAll();
        HandlerList.unregisterAll(this);
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
