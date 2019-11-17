package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 聊天拦截器
 */
public class ChatInterceptor {
    private ChatListener chatListener;
    private JavaPlugin plugin;
    private Player player;
    private int timeout;
    private long creationTime;

    protected ChatInterceptor(ChatListener chatListener, Player player, JavaPlugin plugin, int timeout) {
        this.player = player;
        this.plugin = plugin;
        this.chatListener = chatListener;
        this.timeout = timeout;
        this.creationTime = System.currentTimeMillis();
    }

    public boolean isTimeout() {
        return timeout != -1 && ((System.currentTimeMillis() - creationTime) / 1000L > timeout);
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    public Player getPlayer() {
        return player;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void unregister() {
        JulyChatFilter.unregisterChatFilter(player);
    }
}
