package com.github.julyss2019.mcsp.julylibrary;

import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilder;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryListenerCaller;
import com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener;
import com.github.julyss2019.mcsp.julylibrary.inventory.ListenerItem;
import com.github.julyss2019.mcsp.julylibrary.logger.JulyFileLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class JulyLibrary extends JavaPlugin {
    private static JulyLibrary instance;

    @Override
    public void onEnable() {
        instance = this;

        JulyChatFilter.init();
        JulyFileLogger.init();
        InventoryBuilder.init();
        getLogger().info("插件初始化完毕!");
        onTest();

        getCommand("jl").setExecutor((cs, command, s, args) -> {
            if (args.length == 1 && args[0].equalsIgnoreCase("inv")) {
                int itemTotal = 0;

                for (List<ListenerItem> listenerItems : InventoryBuilder.getInventoryListenerCaller().getItemListenerMap().values()) {
                    itemTotal += listenerItems.size();
                }

                cs.sendMessage("item_listener: " + itemTotal);
                cs.sendMessage("inventory_listener: " + InventoryBuilder.getInventoryListenerCaller().getInventoryListenerMap().size());
                return true;
            }

            return false;
        });
    }

    public void onTest() {

    }

    @Override
    public void onDisable() {
        JulyFileLogger.closeLoggers();
        Bukkit.getScheduler().cancelTasks(this);
    }

    public static JulyLibrary getInstance() {
        return instance;
    }
}
