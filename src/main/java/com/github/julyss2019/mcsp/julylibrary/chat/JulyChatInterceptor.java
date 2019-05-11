package com.github.julyss2019.mcsp.julylibrary.chat;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;

public class JulyChatInterceptor {
    private static HashMap<@NotNull String, @NotNull JulyChatInterceptor> chatInterceptors = new HashMap<>();
    private ChatCallBack callback;

    private JulyChatInterceptor(ChatCallBack callback) {
        this.callback = callback;
    }

    /**
     * 获得玩家的聊天拦截器
     * @param playerName
     * @return
     */
    public static JulyChatInterceptor getChatInterceptor(String playerName) {
        return chatInterceptors.get(playerName);
    }

    /**
     * 获得玩家的聊天拦截器
     * @param player
     * @return
     */
    public static JulyChatInterceptor getChatInterceptor(Player player) {
        return chatInterceptors.get(player.getName());
    }

    /**
     * 玩家是否注册了聊天拦截器
     * @param playerName
     * @return
     */
    public static boolean hasChatInterceptor(String playerName) {
        return chatInterceptors.containsKey(playerName);
    }

    /**
     * 玩家是否注册了聊天拦截器
     * @param player
     * @return
     */
    public static boolean hasChatInterceptor(Player player) {
        return chatInterceptors.containsKey(player.getName());
    }

    /**
     * 卸载所有聊天拦截器
     */
    public static void unregisterAllChatInterceptors() {
        chatInterceptors.clear();
    }

    /**
     * 得到所有聊天拦截器
     * @return
     */
    public static Collection<JulyChatInterceptor> getChatInterceptors() {
        return chatInterceptors.values();
    }

    /**
     * 卸载聊天拦截器
     * @param playerName
     */
    public static void unregisterChatInterceptor(String playerName) {
        chatInterceptors.remove(playerName);
    }

    /**
     * 卸载聊天拦截器
     * @param player
     */
    public static void unregisterChatInterceptor(Player player) {
        chatInterceptors.remove(player.getName());
    }

    /**
     * 创建一个聊天拦截器
     * @param player 玩家
     * @param chatCallBack 拦截回调
     * @return
     */
    public static JulyChatInterceptor registerChatInterceptor(Player player, ChatCallBack chatCallBack) {
        String playerName = player.getName();

        if (chatInterceptors.containsKey(playerName)) {
            throw new IllegalArgumentException("该玩家已有一个聊天拦截器");
        }

        JulyChatInterceptor instance = new JulyChatInterceptor(chatCallBack);

        chatInterceptors.put(player.getName(), instance);
        return instance;
    }

    /**
     * 回调
     * @return
     */
    public ChatCallBack getCallback() {
        return callback;
    }
}
