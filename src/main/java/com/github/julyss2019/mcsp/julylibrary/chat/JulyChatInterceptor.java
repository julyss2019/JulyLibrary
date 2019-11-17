package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class JulyChatInterceptor {
    private Map<String, ChatInterceptor> playerChatInterceptorMap = new HashMap<>();

    public static ChatInterceptor registerChatInterceptor(@NotNull ChatListener chatListener, Player player, JavaPlugin plugin) {

    }

    public static ChatInterceptor registerChatInterceptor(@NotNull ChatListener chatListener, Player player, JavaPlugin plugin, int timeout) {

    }
}
