package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.entity.Player;

public class ChatFilter {
    private ChatListener chatListener;
    private Player player;

    public ChatFilter(Player player, ChatListener chatListener) {
        this.player = player;
        this.chatListener = chatListener;
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
