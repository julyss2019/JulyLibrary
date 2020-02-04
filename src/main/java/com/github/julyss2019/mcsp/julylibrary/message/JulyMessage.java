package com.github.julyss2019.mcsp.julylibrary.message;

import com.github.julyss2019.mcsp.julylibrary.utils.NMSUtil;
import com.github.julyss2019.mcsp.julylibrary.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JulyMessage {
    private static final List<String> TITLE_NMS_VERSIONS = Arrays.asList("1_8_R1", "1_8_R2", "1_8_R3", "1_9_R1", "1_9_R2", "1_10_R1", "1_11_R1", "1_12_R1", "1_13_R1", "1_13_R2", "1_14_R1", "1_15_R1");
    private static final List<String> RAW_NMS_VERSIONS = Arrays.asList("1.7_R1", "1.7_R2", "1.7_R3", "1.7_R4", "1_8_R1", "1_8_R2", "1_8_R3", "1_9_R1", "1_9_R2", "1_10_R1", "1_11_R1", "1_12_R1", "1_13_R1", "1_13_R2", "1_14_R1", "1_15_R1");
    private static Class<?> ichatBaseComponentClass;
    private static Class<?> packetPlayOutTitleClass;
    private static Class<?> titleActionClass = null;
    private static Class<?> packetPlayOutChatClass;
    private static Class<?> v1_7_ChatSerializerClass;

    static {
        try {
            ichatBaseComponentClass = NMSUtil.getNMSClass("IChatBaseComponent");
            packetPlayOutTitleClass = NMSUtil.getNMSClass("PacketPlayOutTitle");
            packetPlayOutChatClass = NMSUtil.getNMSClass("PacketPlayOutChat");

            if (packetPlayOutTitleClass != null) {
                titleActionClass = packetPlayOutTitleClass.getDeclaredClasses()[0];
            }
        } catch (Exception ignored) {}

        if (NMSUtil.NMS_VERSION.equals("v1_7_R4") || NMSUtil.NMS_VERSION.equals("v1_7_R3") || NMSUtil.NMS_VERSION.equals("v1_7_R2") || NMSUtil.NMS_VERSION.equals("v1_7_R1")) {
            v1_7_ChatSerializerClass = NMSUtil.getNMSClass("ChatSerializer");
        }
    }

    /**
     * 广播 Raw 消息
     * @param json
     * @return
     */
    public static void broadcastRawMessage(@NotNull String json) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendRawMessage(player, json);
        }
    }

    /**
     * 发送 Raw 消息
     * @param player
     * @param json
     * @return
     */
    public static void sendRawMessage(@NotNull Player player, @NotNull String json) {
        if (!canSendRawMessage()) {
            throw new RuntimeException("当前版本不支持发送 Raw: " + NMSUtil.NMS_VERSION);
        }

        // 1.7版本支持
        if (NMSUtil.NMS_VERSION.equals("v1_7_R4") || NMSUtil.NMS_VERSION.equals("v1_7_R3") || NMSUtil.NMS_VERSION.equals("v1_7_R2") || NMSUtil.NMS_VERSION.equals("v1_7_R1")) {
            try {
                Object chatBaseComponent = v1_7_ChatSerializerClass.getMethod("a", String.class).invoke(null, json);
                Object packet = packetPlayOutChatClass.getConstructor(ichatBaseComponentClass).newInstance(chatBaseComponent);

                PlayerUtil.sendPacket(player, packet);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                throw new RuntimeException(e);
            }

            return;
        }

        try {
            Object chatBaseComponent = ichatBaseComponentClass.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, json);
            Object packet = packetPlayOutChatClass.getConstructor(ichatBaseComponentClass).newInstance(chatBaseComponent);

            PlayerUtil.sendPacket(player, packet);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * List<String> 着色
     * @param messages
     * @return
     */
    public static List<String> toColoredMessages(@NotNull List<String> messages) {
        List<String> result = new ArrayList<>();

        messages.forEach(s -> result.add(toColoredMessage(s)));
        return result;
    }

    /**
     * 文字着色
     * @param s
     * @return
     */
    public static String toColoredMessage(@NotNull String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * 发送一条空行
     * @param cs
     */
    public static void sendBlankLine(@NotNull CommandSender cs) {
        sendColoredMessage(cs, "");
    }

    /**
     * 广播带颜色的消息
     * @param msg
     */
    public static void broadcastColoredMessage(@NotNull String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendColoredMessage(player, msg);
        }
    }

    /**
     * 发送多条消息
     * @param cs
     * @param messages
     */
    public static void sendColoredMessages(CommandSender cs, @NotNull String... messages) {
        for (String msg : messages) {
            sendColoredMessage(cs, msg);
        }
    }

    /**
     * 发送多条消息
     * @param cs
     * @param messages
     */
    public static void sendColoredMessages(@NotNull CommandSender cs, @NotNull List<String> messages) {
        for (String msg : messages) {
            sendColoredMessage(cs, msg);
        }
    }

    /**
     * 发送带颜色的消息
     * @param cs
     * @param msg
     */
    public static void sendColoredMessage(@NotNull CommandSender cs, @NotNull String msg) {
        cs.sendMessage(toColoredMessage(msg));
    }

    /**
     * 广播 Title
     * @param title
     * @return
     */
    public static void broadcastTitle(@NotNull Title title) {
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
    public static void sendTitle(@NotNull Player player, @NotNull Title title) {
        if (!canUseTitle()) {
            throw new RuntimeException("当前版本不支持发送 Title: " + NMSUtil.NMS_VERSION);
        }

        try {
            Object titleAction = titleActionClass.getField(title.getTitleType().name()).get(null); // 因为是 Enum 类，所以 Object 用 null
            Object chatBaseComponent = ichatBaseComponentClass.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title.getText() + "\"}"); // 是静态方法，所以用 null
            Object packet = packetPlayOutTitleClass.getDeclaredConstructor(titleActionClass, ichatBaseComponentClass, int.class, int.class, int.class)
                    .newInstance(titleAction, chatBaseComponent, title.getFadeIn(), title.getStay(), title.getFadeOut());

            PlayerUtil.sendPacket(player, packet);
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 能否使用 Title
     * @return
     */
    public static boolean canUseTitle() {
        return TITLE_NMS_VERSIONS.contains(NMSUtil.NMS_VERSION);
    }

    /**
     * 能否使用 raw
     * @return
     */
    public static boolean canSendRawMessage() {
        return RAW_NMS_VERSIONS.contains(NMSUtil.NMS_VERSION);
    }
}
