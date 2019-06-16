package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListenerCaller implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (JulyChatFilter.hasChatFilter(player)) {
            ChatListener chatListener = JulyChatFilter.getChatFilter(player).getChatListener();

            chatListener.onChat(event);
        }
    }
}
