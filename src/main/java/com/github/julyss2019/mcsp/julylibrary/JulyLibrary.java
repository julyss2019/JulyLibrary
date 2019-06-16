package com.github.julyss2019.mcsp.julylibrary;

import com.github.julyss2019.mcsp.julylibrary.logger.JulyFileLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class JulyLibrary extends JavaPlugin {
    private static JulyLibrary instance;

    @Override
    public void onEnable() {
        instance = this;

        JulyFileLogger.init();
        getLogger().info("插件初始化完毕!");
        onTest();
    }

    public void onTest() {
        // System.out.println(new SkullItemBuilder().owner("July_ss").material(Material.SADDLE).durability((short) 3).build());
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
