package com.github.julyss2019.mcsp.julylibrary.message;

import com.github.julyss2019.mcsp.julylibrary.utils.MessageUtil;
import com.github.julyss2019.mcsp.julylibrary.utils.NMSUtil;
import com.github.julyss2019.mcsp.julylibrary.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class JulyMessage {
    private static Class<?> chatBaseComponentClass = null;
    private static Class<?> packetPlayOutTitleClass = null;
    private static Class<?> titleActionClass = null;
    private static Class<?> packetPlayOutChatClass = null;

    /*
    初始化 Title 需要的类
     */
    static {
        try {
            chatBaseComponentClass = NMSUtil.getNMSClass("IChatBaseComponent");
            packetPlayOutTitleClass = NMSUtil.getNMSClass("PacketPlayOutTitle");
            packetPlayOutChatClass = NMSUtil.getNMSClass("PacketPlayOutChat");

            if (packetPlayOutTitleClass != null) {
                titleActionClass = packetPlayOutTitleClass.getDeclaredClasses()[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 广播 Raw 消息
     * @param json
     * @return
     */
    public static boolean broadcaseRawMessage(String json) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!sendRawMessage(player, json)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 发送 Raw 消息
     * @param player
     * @param json
     * @return
     */
    public static boolean sendRawMessage(Player player, String json) {
        if (NMSUtil.SERVER_VERSION.equals("v1_7_R4")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + MessageUtil.translateColorCode(json));
            return true;
        }

        try {
            Object chatBaseComponent = chatBaseComponentClass.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, json);
            Object packet = packetPlayOutChatClass.getConstructor(chatBaseComponentClass).newInstance(chatBaseComponent);

            if (!PlayerUtil.sendPacket(player, packet)) {
                return false;
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static String toColoredMessage(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * 发送一条空行
     * @param cs
     */
    public static void sendBlankLine(CommandSender cs) {
        sendColoredMessage(cs, "");
    }

    /**
     * 广播带颜色的消息
     * @param msg
     */
    public static void broadcastColoredMessage(String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendColoredMessage(player, msg);
        }
    }

    public static void sendColoredMessages(CommandSender cs, String... messages) {
        for (String msg : messages) {
            sendColoredMessage(cs, msg);
        }
    }

    public static void sendColoredMessages(CommandSender cs, List<String> messages) {
        for (String msg : messages) {
            sendColoredMessage(cs, msg);
        }
    }

    /**
     * 发送带颜色的消息（带前缀）
     * @param cs
     * @param msg
     */
    public static void sendColoredMessage(CommandSender cs, String msg) {
        cs.sendMessage(toColoredMessage(msg));
    }

    /**
     * 广播 Title
     * @param title
     * @return
     */
    public static void broadcastTitle(Title title) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendTitle(player, title);
        }
    }

    /**
     * 发送 Title
     * @param player
     * @param title
     * @return
     */
    public static void sendTitle(Player player, Title title) {
        if (!canUseTitle()) {
            throw new TitleException("当前服务器版本不支持Title");
        }

        try {
            Object titleAction = titleActionClass.getField(title.getTitleType().name()).get(null); // 因为是 Enum 类，所以 Object 用 null
            Object chatBaseComponent = chatBaseComponentClass.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title.getText() + "\"}"); // 是静态方法，所以用 null
            Object packet = packetPlayOutTitleClass.getDeclaredConstructor(titleActionClass, chatBaseComponentClass, int.class, int.class, int.class)
                    .newInstance(titleAction, chatBaseComponent, title.getFadeIn(), title.getStay(), title.getFadeOut());

            PlayerUtil.sendPacket(player, packet);
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否可以使用 Title
     * @return
     */
    public static boolean canUseTitle() {
        return packetPlayOutTitleClass != null;
    }

    public static void sendColoredTitle(Player player, String text) {
        sendTitle(player, new TitleBuilder().type(TitleType.TITLE).text(text).colored().build());
    }

    public static void sendColoredSubTitle(Player player, String text) {
        sendTitle(player, new TitleBuilder().type(TitleType.SUBTITLE).text(text).colored().build());
    }
}
