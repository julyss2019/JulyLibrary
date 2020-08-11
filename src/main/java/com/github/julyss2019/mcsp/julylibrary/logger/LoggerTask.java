package com.github.julyss2019.mcsp.julylibrary.logger;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class LoggerTask extends BukkitRunnable {
    private Map<Logger, Long> lastFlushMap = new HashMap<>();

    @Override
    public void run() {
        for (Logger logger : JulyLibrary.getInstance().getLoggerManager().getLoggers()) {
            Logger.Storage storage = logger.getStorage();

            if (storage != null && (System.currentTimeMillis() - lastFlushMap.getOrDefault(logger, 0L) > storage.getFlushInterval() * 1000L)) {
                logger.flush();
                lastFlushMap.put(logger, System.currentTimeMillis());
            }
        }
    }
}
