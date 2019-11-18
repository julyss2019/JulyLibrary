package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatEventFirer implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (JulyChatInterceptor.isRegistered(player)) {
            ChatInterceptor chatInterceptor = JulyChatInterceptor.getChatInterceptor(player);
            ChatListener chatListener = chatInterceptor.getChatListener();

            if (chatInterceptor.getTimeout()) {
                event.setCancelled(true);
                chatListener.onTimeout();
            } else {
                event.setCancelled(true);
                chatListener.onChat(event);
            }
        }

        // 弃用
        if (JulyChatFilter.hasChatFilter(player)) {
            ChatFilter chatFilter = JulyChatFilter.getChatFilter(player);
            ChatListener chatListener = chatFilter.getChatListener();

            if (!chatFilter.isTimeout()) {
                event.setCancelled(true);
                chatListener.onChat(event);
            } else {
                chatListener.onTimeout();
                event.setCancelled(true);
                JulyChatFilter.unregisterChatFilter(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        JulyChatFilter.unregisterChatFilter(player);
        JulyChatInterceptor.unregister(player);
    }
}
