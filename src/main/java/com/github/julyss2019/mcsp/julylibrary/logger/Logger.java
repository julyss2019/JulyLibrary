package com.github.julyss2019.mcsp.julylibrary.logger;


import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import com.github.julyss2019.mcsp.julylibrary.text.JulyText;
import com.github.julyss2019.mcsp.julylibrary.text.PlaceholderContainer;
import com.github.julyss2019.mcsp.julylibrary.utilv2.ValidateUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

public class Logger {
    private static final SimpleDateFormat DATE_TIME_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_SDF = new SimpleDateFormat("yyyy-MM-dd");

    public enum Level {
        DEBUG(0, 'e'), INFO(1, 'f'), WARNING(2, 'e'), ERROR(3, 'c');

        int value;
        char color;

        Level(int value, char color) {
            this.value = value;
            this.color = color;
        }

        public char getColor() {
            return color;
        }

        public int getValue() {
            return value;
        }
    }


/*    @Deprecated
    public static final class Builder {
        private Plugin plugin;
        private Level consoleLevel = Level.INFO;
        private Storage storage;
        private String consoleFormat = "&a[${plugin_name}] &f[${level}] &f${msg}";
        private String storageFormat = "[${date_time}] [${level}] ${msg}";

        public Builder() {
        }

        public Builder plugin(Plugin plugin) {
            this.plugin = plugin;
            return this;
        }

        public Builder storage(Storage storage) {
            this.storage = storage;
            return this;
        }

        public Builder consoleFormat(String consoleFormat) {
            this.consoleFormat = consoleFormat;
            return this;
        }

        public Builder storageFormat(String storageFormat) {
            this.storageFormat = storageFormat;
            return this;
        }

        public Builder consoleLevel(Level level) {
            this.consoleLevel = level;
            return this;
        }

        public Logger build() {
            return new Logger(this);
        }
    }*/


    public static class Storage {
        private File folder;
        private String fileName;
        private int flushInterval;

        public Storage(@NotNull File folder, @NotNull String fileName, int flushInterval) {
            if (flushInterval < 0) {
                throw new IllegalArgumentException("flushInterval 必须 >= 0");
            }

            this.folder = folder;
            this.fileName = fileName;
            this.flushInterval = flushInterval;
        }

        public File getFolder() {
            return folder;
        }

        public String getFileName() {
            return fileName;
        }

        public int getFlushInterval() {
            return flushInterval;
        }
    }


    private Level consoleLevel = Level.INFO;
    private Level storageLevel = Level.INFO;
    private String consoleFormat = "&a[${plugin_name}] &f[${level}] &f${msg}";
    private String storageFormat = "[${date_time}] [${level}] ${msg}";
    private Plugin plugin;
    private String pluginName;
    private Storage storage;

    private long time;

    private File file;
    private Writer fileWriter;

    Logger(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.pluginName = plugin.getName();
    }

    public Level getStorageLevel() {
        return storageLevel;
    }

    public void setStorageLevel(@NotNull Level storageLevel) {
        this.storageLevel = storageLevel;
    }

    public void setConsoleLevel(@NotNull Level consoleLevel) {
        this.consoleLevel = consoleLevel;
    }

    public void setConsoleFormat(@NotNull String consoleFormat) {
        this.consoleFormat = consoleFormat;
    }

    public void setStorageFormat(@NotNull String storageFormat) {
        this.storageFormat = storageFormat;
    }

    public void setStorage(@NotNull Storage storage) {
        this.storage = storage;
    }

    public Level getConsoleLevel() {
        return consoleLevel;
    }

    public String getConsoleFormat() {
        return consoleFormat;
    }

    public String getStorageFormat() {
        return storageFormat;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Storage getStorage() {
        return storage;
    }

    public void info(@NotNull String msg) {
        log(Level.INFO, msg);
    }

    public void debug(@NotNull String msg) {
        log(Level.DEBUG, msg);
    }

    public void warning(@NotNull String msg) {
        log(Level.WARNING, msg);
    }

    public void error(@NotNull String msg) {
        log(Level.ERROR, msg);
    }

    public void log(@NotNull Level level, @NotNull String msg) {
        this.time = System.currentTimeMillis();

        if (level.getValue() >= consoleLevel.getValue()) {
            JulyMessage.sendColoredMessage(Bukkit.getConsoleSender(), JulyText.setPlaceholders(consoleFormat, new PlaceholderContainer()
                    .add("plugin_name", pluginName)
                    .add("level", level.name())
                    .add("msg", "§" + level.getColor() + msg)));
        }

        if (level.getValue() >= storageLevel.getValue() && storage != null) {
            write(JulyText.setPlaceholders(storageFormat, new PlaceholderContainer()
                    .add("date_time", DATE_TIME_SDF.format(time))
                    .add("plugin_name", pluginName)
                    .add("level", level.name())
                    .add("msg", msg)));
        }
    }

    private File getNewFile() {
        ValidateUtil.notNull(storage);

        return new File(storage.folder, JulyText.setPlaceholders(storage.fileName, new PlaceholderContainer()
                .add("date", DATE_SDF.format(time))));
    }

    private void update() {
        File newFile = getNewFile();
        File folder = newFile.getParentFile();

        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("创建文件夹失败: " + folder.getAbsolutePath());
        }

        if (!newFile.equals(file)) {
            if (fileWriter != null) {
                try {
                    fileWriter.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            this.file = newFile;

            try {
                if (!file.exists() && !file.createNewFile()) {
                    throw new RuntimeException("文件创建失败: " + file.getAbsolutePath());
                }

                this.fileWriter = new OutputStreamWriter(new FileOutputStream(newFile, true), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void write(@NotNull String s) {
        update();

        try {
            fileWriter.write(s);
            fileWriter.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void flush() {
        if (fileWriter != null) {
            try {
                fileWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
