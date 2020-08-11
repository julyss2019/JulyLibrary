package com.github.julyss2019.mcsp.julylibrary.logger;



import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class LoggerManager {
    private Set<Logger> loggers = new HashSet<>();

    /*
    因为涉及到自动保存所以需要集中管理
    Builder 模式不伦不类
     */
    public Logger createLogger(@NotNull Plugin plugin) {
        Logger logger = new Logger(plugin);

        registerLogger(logger);
        return logger;
    }

    public void registerLogger(@NotNull Logger logger) {
        loggers.add(logger);
    }

    public void unregisterLogger(@NotNull Logger logger) {
        loggers.add(logger);
    }

    public Set<Logger> getLoggers() {
        return new HashSet<>(loggers);
    }
}
