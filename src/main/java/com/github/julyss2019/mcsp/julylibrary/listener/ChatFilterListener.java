package com.github.julyss2019.mcsp.julylibrary.listener;

import com.github.julyss2019.mcsp.julylibrary.chat.JulyChatFilter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatFilterListener implements Listener {
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        JulyChatFilter.unregisterChatFilter(event.getPlayer());
    }
}
