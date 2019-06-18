package com.github.julyss2019.mcsp.julylibrary.chat;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;

public class JulyChatFilter {
    private static JulyLibrary plugin = JulyLibrary.getInstance();
    private static HashMap<@NotNull String, @NotNull ChatFilter> chatFilterMap = new HashMap<>();

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new ChatListenerCaller(), plugin);
    }

    /**
     * 获得玩家的聊天过滤器
     * @param player
     * @return
     */
    public static ChatFilter getChatFilter(Player player) {
        return chatFilterMap.get(player.getName());
    }

    /**
     * 玩家是否注册了聊天过滤器
     * @param player
     * @return
     */
    public static boolean hasChatFilter(Player player) {
        return chatFilterMap.containsKey(player.getName());
    }

    /**
     * 卸载所有聊天过滤器
     */
    public static void unregisterAll() {
        chatFilterMap.clear();
    }

    /**
     * 得到所有聊天过滤器
     * @return
     */
    public static Collection<ChatFilter> getChatFilters() {
        return chatFilterMap.values();
    }

    /**
     * 卸载聊天过滤器
     * @param player
     */
    public static void unregisterChatFilter(Player player) {
        chatFilterMap.remove(player.getName());
    }

    /**
     * 创建一个聊天过滤器
     * @param player 玩家
     * @param chatListener 拦截回调
     * @return
     */
    public static boolean registerChatFilter(Player player, @NotNull ChatListener chatListener) {
        String playerName = player.getName();

        if (chatFilterMap.containsKey(playerName)) {
            return false;
        }

        chatFilterMap.put(player.getName(), new ChatFilter(player, chatListener));
        return true;
    }
}
