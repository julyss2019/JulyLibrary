package com.github.julyss2019.mcsp.julylibrary.chat;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatInterceptorListener implements Listener {
    private ChatInterceptorManager chatInterceptorManager = JulyLibrary.getInstance().getChatInterceptorManager();

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (chatInterceptorManager.hasChatInterceptor(player)) {
            ChatInterceptor chatInterceptor = chatInterceptorManager.getChatInterceptor(player);
            ChatListener chatListener = chatInterceptor.getChatListener();

            event.setCancelled(true);

            // 超时
            if (chatInterceptor.isTimeout()) {
                chatListener.onTimeout(event);
            } else {
                chatListener.onChat(event);
            }

            if (chatInterceptor.isOnlyFirst()) {
                chatInterceptorManager.unregisterChatInterceptor(player);
            }
        }
    }

    /*
    离线后注销拦截器
     */
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (chatInterceptorManager.hasChatInterceptor(player)) {
            chatInterceptorManager.unregisterChatInterceptor(player);
        }
    }
}
