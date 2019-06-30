package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.entity.Player;

public class ChatFilter {
    private ChatListener chatListener;
    private Player player;
    private int timeout;
    private long creationTime;

    protected ChatFilter(Player player, ChatListener chatListener, int timeout) {
        this.player = player;
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

    public void unregister() {
        JulyChatFilter.unregisterChatFilter(player);
    }
}
