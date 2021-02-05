package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ChatInterceptorManager {
    private final Map<UUID, ChatInterceptor> playerChatInterceptorMap = new HashMap<>();

    /**
     * 注册聊天拦截器
     * @param chatInterceptor
     */
    public void registerChatInterceptor(@NotNull ChatInterceptor chatInterceptor) {
        if (!chatInterceptor.getPlayer().isOnline()) {
            throw new RuntimeException("玩家不在线");
        }

        playerChatInterceptorMap.put(chatInterceptor.getPlayer().getUniqueId(), chatInterceptor);
    }

    /**
     * 得到一个插件的所有聊天拦截器
     * @param plugin
     * @return
     */
    public Set<ChatInterceptor> getChatInterceptors(@NotNull Plugin plugin) {
        return getChatInterceptors().stream().filter(chatInterceptor -> plugin.equals(chatInterceptor.getPlugin())).collect(Collectors.toSet());
    }

    /**
     * 得到所有聊天拦截器
     * @return
     */
    public Set<ChatInterceptor> getChatInterceptors() {
        return new HashSet<>(playerChatInterceptorMap.values());
    }

    /**
     * 注销所有的聊天拦截器
     */
    public void unregisterAll() {
        playerChatInterceptorMap.clear();
    }

    /**
     * 卸载一个 Plugin 的所有聊天拦截器
     * @param plugin
     */
    public void unregisterAll(@NotNull Plugin plugin) {
        Iterator<Map.Entry<UUID, ChatInterceptor>> iterator = playerChatInterceptorMap.entrySet().iterator();

        while (iterator.hasNext()) {
            ChatInterceptor chatInterceptor = iterator.next().getValue();

            if (chatInterceptor.getPlugin().equals(plugin)) {
                iterator.remove();
            }
        }
    }

    /**
     * 得到玩家的聊天拦截器
     * @param player 玩家
     * @return
     */
    public ChatInterceptor getChatInterceptor(@NotNull Player player) {
        return playerChatInterceptorMap.get(player.getUniqueId());
    }

    /**
     * 是否已注册聊天拦截器
     * @param player 玩家
     * @return
     */
    public boolean hasChatInterceptor(@NotNull Player player) {
        return playerChatInterceptorMap.containsKey(player.getUniqueId());
    }

    /**
     * 注销聊天拦截器
     * @param player 玩家
     */
    public void unregisterChatInterceptor(@NotNull Player player) {
        if (!playerChatInterceptorMap.containsKey(player.getUniqueId())) {
            throw new RuntimeException("未注册 ChatInterceptor");
        }

        playerChatInterceptorMap.remove(player.getUniqueId());
    }
}
