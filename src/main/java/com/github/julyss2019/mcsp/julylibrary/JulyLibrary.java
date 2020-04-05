package com.github.julyss2019.mcsp.julylibrary;

import com.github.julyss2019.mcsp.julylibrary.chat.ChatInterceptorListener;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatInterceptorManager;
import com.github.julyss2019.mcsp.julylibrary.inventory.InventoryBuilderListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class JulyLibrary extends JavaPlugin implements Listener {
    private static JulyLibrary instance;
    private ChatInterceptorManager chatInterceptorManager;
    private InventoryBuilderListener inventoryBuilderListener;

    @Override
    public void onEnable() {
        instance = this;
        this.chatInterceptorManager = new ChatInterceptorManager();

        Bukkit.getPluginManager().registerEvents(this.inventoryBuilderListener = new InventoryBuilderListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatInterceptorListener(), this);
        getCommand("JulyLibrary").setExecutor((cs, command, s, args) -> {
            if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
                cs.sendMessage("§f插件版本: " + getDescription().getVersion() + ".");
                return true;
            }

            cs.sendMessage("§f/jlib version - 显示插件版本");
            return true;
        });

        JulyLibraryLogger.info("插件版本: v" + getDescription().getVersion() + ".");
        JulyLibraryLogger.info("插件初始化完毕.");
    }

    public ChatInterceptorManager getChatInterceptorManager() {
        return chatInterceptorManager;
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        chatInterceptorManager.unregisterAll();
        HandlerList.unregisterAll((Plugin) instance);
        JulyLibraryLogger.info("插件被卸载.");
    }

    /**
     * 得到背包事件触发器
     * @return
     */
    public InventoryBuilderListener getInventoryBuilderListener() {
        return inventoryBuilderListener;
    }

    public static JulyLibrary getInstance() {
        return instance;
    }
}
