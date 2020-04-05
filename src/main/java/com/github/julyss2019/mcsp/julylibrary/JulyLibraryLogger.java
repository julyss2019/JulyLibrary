package com.github.julyss2019.mcsp.julylibrary;

import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class JulyLibraryLogger {
    public static void error(@NotNull String msg) {
        sendConsoleMsg("&c" + msg);
    }

    public static void warning(@NotNull String msg) {
        sendConsoleMsg("&e" + msg);
    }

    public static void info(@NotNull String msg) {
        sendConsoleMsg("&f" + msg);
    }

    private static void sendConsoleMsg(@NotNull String msg) {
        JulyMessage.sendColoredMessage(Bukkit.getConsoleSender(), "&a[JulyLibrary] &f" + msg);
    }
}
