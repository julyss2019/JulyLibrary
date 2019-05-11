package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (JulyChatInterceptor.hasChatInterceptor(player)) {
            JulyChatInterceptor chatInterceptor = JulyChatInterceptor.getChatInterceptor(player);

            chatInterceptor.getCallback().setMessage(event.getMessage());
            event.setCancelled(true);
            JulyChatInterceptor.unregisterChatInterceptor(player);
        }
    }
}
