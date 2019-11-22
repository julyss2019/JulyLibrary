package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface ChatListener {
    /**
     * 聊天事件
     * @param event
     */
    default void onChat(AsyncPlayerChatEvent event) {}
    @Deprecated
    default void onTimeout() {}

    /**
     * 超时事件
     * @param event
     */
    default void onTimeout(AsyncPlayerChatEvent event) {}
}
