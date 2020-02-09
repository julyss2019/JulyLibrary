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

        if (ChatInterceptorManager.hasChatInterceptor(player)) {
            ChatInterceptor chatInterceptor = ChatInterceptorManager.getChatInterceptor(player);
            ChatListener chatListener = chatInterceptor.getChatListener();

            event.setCancelled(true);

            // 超时
            if (chatInterceptor.isTimeout()) {
                chatListener.onTimeout(event);
            } else {
                chatListener.onChat(event);
            }

            if (chatInterceptor.isOnlyFirst()) {
                ChatInterceptorManager.unregisterChatInterceptor(player);
            }
        }
    }

    /*
    离线后注销拦截器
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ChatInterceptorManager.unregisterChatInterceptor(player);
    }
}
