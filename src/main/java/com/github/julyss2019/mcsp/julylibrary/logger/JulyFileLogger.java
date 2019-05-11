package com.github.julyss2019.mcsp.julylibrary.logger;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JulyFileLogger {
    private static List<@NotNull FileLogger> loggers = new ArrayList<>();

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (FileLogger fileLogger : loggers) {
                    fileLogger.update();
                }
            }
        }.runTaskTimer(JulyLibrary.getInstance(), 0L, 20L);
    }

    /**
     * 创建一个 Logger
     * @param loggerFolder 文件夹，变量：%DATE% 日期
     * @param fileName 文件名，为空则为 %DATE%
     * @param saveInterval 保存间隔，单位为秒
     * @return
     */
    public static FileLogger getLogger(@NotNull File loggerFolder, @Nullable String fileName, int saveInterval) {
        if (saveInterval < 0)
        {
            throw new IllegalArgumentException("保存间隔必须大于等于0");
        }

        FileLogger instance = new FileLogger(loggerFolder, fileName, saveInterval);

        loggers.add(instance);
        return instance;
    }

    /**
     * 关闭所有 Logger
     */
    public static void closeLoggers() {
        for (FileLogger fileLogger : loggers) {
            fileLogger.close();
        }

        loggers.clear();
    }
}
