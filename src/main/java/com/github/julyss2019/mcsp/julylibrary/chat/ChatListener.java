package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface ChatListener {
    default void onChat(AsyncPlayerChatEvent event) {}
    default void onTimeout() {}
}
