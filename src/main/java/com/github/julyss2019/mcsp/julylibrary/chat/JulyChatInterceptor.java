package com.github.julyss2019.mcsp.julylibrary.chat;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Deprecated
public class JulyChatInterceptor {
    /**
     * 注册聊天拦截器
     * @param chatInterceptor
     */
    public static void registerChatInterceptor(ChatInterceptor chatInterceptor) {
        JulyLibrary.getInstance().getChatInterceptorManager().registerChatInterceptor(chatInterceptor);
    }

    /**
     * 得到一个插件的所有聊天拦截器
     * @param plugin
     * @return
     */
    public static Collection<ChatInterceptor> getChatInterceptors(@NotNull Plugin plugin) {
        return JulyLibrary.getInstance().getChatInterceptorManager().getChatInterceptors(plugin);
    }

    /**
     * 得到所有聊天拦截器
     * @return
     */
    public static Collection<ChatInterceptor> getChatInterceptors() {
        return JulyLibrary.getInstance().getChatInterceptorManager().getChatInterceptors();
    }

    /**
     * 注销所有的聊天拦截器
     */
    public static void unregisterAll() {
        JulyLibrary.getInstance().getChatInterceptorManager().unregisterAll();
    }

    /**
     * 卸载一个 Plugin 的所有聊天拦截器
     * @param plugin
     */
    public static void unregisterAll(@NotNull Plugin plugin) {
        JulyLibrary.getInstance().getChatInterceptorManager().unregisterAll(plugin);
    }

    /**
     * 得到玩家的聊天拦截器
     * @param player 玩家
     * @return
     */
    public static ChatInterceptor getChatInterceptor(Player player) {
        return JulyLibrary.getInstance().getChatInterceptorManager().getChatInterceptor(player);
    }



    /**
     * 是否已注册聊天拦截器
     * @param player 玩家
     * @return
     */
    public static boolean hasChatInterceptor(Player player) {
        return JulyLibrary.getInstance().getChatInterceptorManager().hasChatInterceptor(player);
    }


    /**
     * 注销聊天拦截器
     * @param player 玩家
     */
    public static void unregisterChatInterceptor(Player player) {
        JulyLibrary.getInstance().getChatInterceptorManager().unregisterChatInterceptor(player);
    }

}
