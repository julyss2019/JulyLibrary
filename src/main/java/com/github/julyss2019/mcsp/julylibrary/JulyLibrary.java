package com.github.julyss2019.mcsp.julylibrary;

import com.github.julyss2019.mcsp.julylibrary.chat.ChatInterceptorListener;
import com.github.julyss2019.mcsp.julylibrary.chat.ChatInterceptorManager;
import com.github.julyss2019.mcsp.julylibrary.inventory.BuilderInventoryListener;
import com.github.julyss2019.mcsp.julylibrary.inventory.BuilderInventoryManager;
import com.github.julyss2019.mcsp.julylibrary.logger.Logger;
import com.github.julyss2019.mcsp.julylibrary.logger.LoggerManager;
import com.github.julyss2019.mcsp.julylibrary.logger.LoggerTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class JulyLibrary extends JavaPlugin implements Listener {
    private static JulyLibrary instance;
    private ChatInterceptorManager chatInterceptorManager;
    private BuilderInventoryManager builderInventoryManager;
    private LoggerManager loggerManager;
    private Logger logger;

    @Override
    public void onEnable() {
        instance = this;
        this.chatInterceptorManager = new ChatInterceptorManager();
        this.builderInventoryManager = new BuilderInventoryManager();
        this.loggerManager = new LoggerManager();
        this.logger = loggerManager.createLogger(this);

        logger.setStorage(new Logger.Storage(new File(getDataFolder(), "logs"), "${date}.log", 10));

        new LoggerTask().runTaskTimer(this, 0L, 20L);
        Bukkit.getPluginManager().registerEvents(new BuilderInventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatInterceptorListener(), this);
        getCommand("JulyLibrary").setExecutor((cs, command, s, args) -> {
            if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
                cs.sendMessage("§f插件版本: " + getDescription().getVersion() + ".");
                return true;
            }

            cs.sendMessage("§f/jlib version - 显示插件版本");
            return true;
        });

        logger.info("插件版本: v" + getDescription().getVersion() + ".");
        logger.info("插件初始化完毕.");
    }

    public Logger getPluginLogger() {
        return logger;
    }

    public LoggerManager getLoggerManager() {
        return loggerManager;
    }

    public ChatInterceptorManager getChatInterceptorManager() {
        return chatInterceptorManager;
    }

    @Override
    public void onDisable() {
        logger.info("插件被卸载.");
        builderInventoryManager.getBuilderInventories().stream().forEach(builderInventory -> {
            builderInventory.getBukkitInventory().getViewers().forEach(HumanEntity::closeInventory);
        });
        Bukkit.getScheduler().cancelTasks(this);
        chatInterceptorManager.unregisterAll();
        HandlerList.unregisterAll((Plugin) instance);
        getLoggerManager().getLoggers().forEach(Logger::flush);
    }

    public BuilderInventoryManager getBuilderInventoryManager() {
        return builderInventoryManager;
    }

    public static JulyLibrary getInstance() {
        return instance;
    }
}
