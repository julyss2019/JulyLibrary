package com.github.julyss2019.mcsp.julylibrary.chat;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JulyChatInterceptor {
    private static Map<String, ChatInterceptor> playerChatInterceptorMap = new HashMap<>();

    public static void registerChatInterceptor(ChatInterceptor chatInterceptor) {
        Validate.notNull(chatInterceptor.getChatListener(), "chatListener 未设置");
        Validate.notNull(chatInterceptor.getPlayerName(), "player 未设置");
        Validate.notNull(chatInterceptor.getPlugin(), "plugin 未设置");

        if (isRegistered(chatInterceptor.getPlayer())) {
            throw new RuntimeException("该玩家已注册 ChatInterceptor");
        }

        playerChatInterceptorMap.put(chatInterceptor.getPlayerName(), chatInterceptor);
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

    public static ChatInterceptor getChatInterceptor(Player player) {
        return getChatInterceptor(player.getName());
    }

    public static ChatInterceptor getChatInterceptor(String playerName) {
        return playerChatInterceptorMap.get(playerName);
    }

    /**
     * 是否已注册聊天拦截器
     * @param player
     * @return
     */
    public static boolean isRegistered(Player player) {
        return isRegistered(player.getName());
    }

    public static boolean isRegistered(String playerName) {
        return playerChatInterceptorMap.containsKey(playerName.toLowerCase());
    }

    public static void unregister(Player player) {
        unregister(player.getName());
    }

    public static void unregister(String playerName) {
        playerChatInterceptorMap.remove(playerName.toLowerCase());
    }
}
