package com.github.julyss2019.mcsp.julylibrary.chat;

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
    private String playerName;
    private int timeout;
    private long creationTime;
    private boolean onlyFirst;

    private ChatInterceptor(Builder builder) {
        setChatListener(builder.chatListener);
        setPlugin(builder.plugin);
        setPlayerName(builder.playerName);
        setTimeout(builder.timeout);
        setOnlyFirst(builder.onlyFirst);
        this.creationTime = System.currentTimeMillis();
    }

    public boolean isOnlyFirst() {
        return onlyFirst;
    }

    /**
     * 设置仅作用一次
     * @param onlyFirst
     */
    public void setOnlyFirst(boolean onlyFirst) {
        this.onlyFirst = onlyFirst;
    }

    /**
     * 设置超时时间
     * @param timeout 单位：秒
     */
    public void setTimeout(int timeout) {
        if (timeout <= 0 && timeout != -1) {
            throw new RuntimeException("timeout 必须 > 0 或 == -1");
        }

        this.timeout = timeout;
    }

    public void setChatListener(ChatListener chatListener) {
        this.chatListener = chatListener;
    }

    private void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    private void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isTimeout() {
        return timeout != -1 && ((System.currentTimeMillis() - creationTime) / 1000L > timeout);
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getPlayerName());
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void register() {
        ChatInterceptorManager.registerChatInterceptor(this);
    }

    public void unregister() {
        ChatInterceptorManager.unregisterChatInterceptor(playerName);
    }

    public static final class Builder {
        private ChatListener chatListener;
        private int timeout = -1;
        private boolean onlyFirst = true;
        private String playerName;
        private Plugin plugin;

        public Builder plugin(@NotNull Plugin plugin) {
            this.plugin = plugin;
            return this;
        }

        public Builder player(Player player) {
            this.playerName = player.getName();
            return this;
        }

        public Builder chatListener(@NotNull ChatListener val) {
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
            Validate.notNull(plugin, "plugin 不能为 null");
            Validate.notNull(chatListener, "chatListener 不能为 null");
            Validate.notNull(playerName, "player 不能为 null");

            return new ChatInterceptor(this);
        }
    }
}
