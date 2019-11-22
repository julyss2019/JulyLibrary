package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class JulyChatInterceptor {
    private static Map<String, ChatInterceptor> playerChatInterceptorMap = new HashMap<>();

    /**
     * 注册聊天拦截器
     * @param chatInterceptor
     */
    public static void registerChatInterceptor(ChatInterceptor chatInterceptor) {
        if (hasChatInterceptor(chatInterceptor.getPlayer())) {
            throw new RuntimeException("该玩家已注册 ChatInterceptor");
        }

        playerChatInterceptorMap.put(chatInterceptor.getPlayerName().toLowerCase(), chatInterceptor);
    }

    /**
     * 得到一个插件的所有聊天拦截器
     * @param plugin
     * @return
     */
    public static Collection<ChatInterceptor> getChatInterceptors(@NotNull Plugin plugin) {
        return getChatInterceptors().stream().filter(chatInterceptor -> plugin.equals(chatInterceptor.getPlugin())).collect(Collectors.toList());
    }

    /**
     * 得到所有聊天拦截器
     * @return
     */
    public static Collection<ChatInterceptor> getChatInterceptors() {
        return playerChatInterceptorMap.values();
    }

    /**
     * 注销所有的聊天拦截器
     */
    public static void unregisterAll() {
        playerChatInterceptorMap.clear();
    }

    /**
     * 卸载一个 Plugin 的所有聊天拦截器
     * @param plugin
     */
    public static void unregisterAll(@NotNull Plugin plugin) {
        Iterator<Map.Entry<String, ChatInterceptor>> iterator = playerChatInterceptorMap.entrySet().iterator();

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
    public static ChatInterceptor getChatInterceptor(Player player) {
        return getChatInterceptor(player.getName());
    }

    /**
     * 得到玩家的聊天拦截器
     * @param playerName 玩家名
     * @return
     */
    public static ChatInterceptor getChatInterceptor(String playerName) {
        return playerChatInterceptorMap.get(playerName.toLowerCase());
    }

    /**
     * 是否已注册聊天拦截器
     * @param player 玩家
     * @return
     */
    public static boolean hasChatInterceptor(Player player) {
        return hasChatInterceptor(player.getName());
    }

    /**
     * 是否已注册聊天拦截器
     * @param playerName 玩家名
     * @return
     */
    public static boolean hasChatInterceptor(String playerName) {
        return playerChatInterceptorMap.containsKey(playerName.toLowerCase());
    }

    /**
     * 注销聊天拦截器
     * @param player 玩家
     */
    public static void unregisterChatInterceptor(Player player) {
        unregisterChatInterceptor(player.getName());
    }

    /**
     * 注销聊天拦截器
     * @param playerName 玩家名
     */
    public static void unregisterChatInterceptor(String playerName) {
        playerChatInterceptorMap.remove(playerName.toLowerCase());
    }
}
