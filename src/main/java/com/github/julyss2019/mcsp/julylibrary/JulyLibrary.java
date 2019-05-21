package com.github.julyss2019.mcsp.julylibrary;

import com.github.julyss2019.mcsp.julylibrary.command.tab.JulyTabCompleter;
import com.github.julyss2019.mcsp.julylibrary.logger.FileLogger;
import com.github.julyss2019.mcsp.julylibrary.logger.JulyFileLogger;
import com.github.julyss2019.mcsp.julylibrary.test.TestCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class JulyLibrary extends JavaPlugin {
    private static JulyLibrary instance;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("插件初始化完毕!");
        test();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    private void test() {
    }

    public static JulyLibrary getInstance() {
        return instance;
    }
}
