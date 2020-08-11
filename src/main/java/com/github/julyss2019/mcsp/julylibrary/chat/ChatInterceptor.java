package com.github.julyss2019.mcsp.julylibrary.chat;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * 聊天拦截器
 */
public class ChatInterceptor {
    private ChatListener chatListener;
    private Plugin plugin;
    private Player player;
    private int timeout;
    private long creationTime;
    private boolean onlyFirst;

    private ChatInterceptor() {}

    private ChatInterceptor(@NotNull Builder builder) {
        Validate.notNull(builder.plugin, "plugin 不能为 null");
        Validate.notNull(builder.chatListener, "chatListener 不能为 null");
        Validate.notNull(builder.player, "player 不能为 null");

        if (timeout < 0) {
            throw new RuntimeException("timeout 必须 >= 0");
        }

        this.chatListener = builder.chatListener;
        this.plugin = builder.plugin;
        this.player = builder.player;
        this.timeout = builder.timeout;
        this.onlyFirst = builder.onlyFirst;
        this.creationTime = System.currentTimeMillis();
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isOnlyFirst() {
        return onlyFirst;
    }

    public boolean isTimeout() {
        return timeout != 0 && ((System.currentTimeMillis() - creationTime) / 1000L > timeout);
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    /*
    修改意见：应该由 Manager 统一管理
     */
    public void register() {
        JulyLibrary.getInstance().getChatInterceptorManager().registerChatInterceptor(this);
    }

    public void unregister() {
        JulyLibrary.getInstance().getChatInterceptorManager().unregisterChatInterceptor(player);
    }

    public static final class Builder {
        private ChatListener chatListener;
        private int timeout = 0;
        private boolean onlyFirst = true;
        private Player player;
        private Plugin plugin;

        public Builder plugin(Plugin plugin) {
            this.plugin = plugin;
            return this;
        }

        public Builder player(Player player) {
            this.player = player;
            return this;
        }

        public Builder chatListener(ChatListener val) {
            chatListener = val;
            return this;
        }

        public Builder timeout(int val) {
            timeout = val;
            return this;
        }

        public Builder onlyFirst(boolean val) {
            onlyFirst = val;
            return this;
        }

        public ChatInterceptor build() {
            return new ChatInterceptor(this);
        }
    }
}
