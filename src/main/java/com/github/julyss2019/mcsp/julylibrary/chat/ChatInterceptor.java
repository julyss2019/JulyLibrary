package com.github.julyss2019.mcsp.julylibrary.chat;

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
    private int timeout = -1;
    private long creationTime;
    private boolean onlyFirst = true;

    private ChatInterceptor(Builder builder) {
        setChatListener(builder.chatListener);
        setPlugin(builder.plugin);
        setPlayerName(builder.playerName);
        setTimeout(builder.timeout);
        creationTime = builder.creationTime;
        setOnlyFirst(builder.onlyFirst);
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
        this.timeout = timeout;
    }

    public void setChatListener(ChatListener chatListener) {
        this.chatListener = chatListener;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean getTimeout() {
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

    public void unregister() {
        JulyChatInterceptor.unregister(playerName);
    }


    public static final class Builder {
        private ChatListener chatListener;
        private Plugin plugin;
        private String playerName;
        private int timeout;
        private long creationTime;
        private boolean onlyFirst;

        public Builder() {
        }

        public Builder chatListener(@NotNull ChatListener val) {
            chatListener = val;
            return this;
        }

        public Builder plugin(@NotNull Plugin val) {
            plugin = val;
            return this;
        }

        public Builder playerName(@NotNull String val) {
            playerName = val;
            return this;
        }

        public Builder timeout(int val) {
            if (timeout < 0) {
                throw new RuntimeException("timeout 必须 >= 0");
            }

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
