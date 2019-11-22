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

        if (JulyChatInterceptor.hasChatInterceptor(player)) {
            ChatInterceptor chatInterceptor = JulyChatInterceptor.getChatInterceptor(player);
            ChatListener chatListener = chatInterceptor.getChatListener();

            event.setCancelled(true);

            // 超时
            if (chatInterceptor.isTimeout()) {
                chatListener.onTimeout();
                chatListener.onTimeout(event);
            } else {
                chatListener.onChat(event);
            }

            if (chatInterceptor.isOnlyFirst()) {
                JulyChatInterceptor.unregisterChatInterceptor(player);
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

    /*
    离线后注销拦截器
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        JulyChatFilter.unregisterChatFilter(player);
        JulyChatInterceptor.unregisterChatInterceptor(player);
    }
}
