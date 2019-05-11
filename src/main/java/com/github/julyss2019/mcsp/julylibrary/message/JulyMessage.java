package com.github.julyss2019.mcsp.julylibrary.message;

import com.github.julyss2019.mcsp.julylibrary.utils.MessageUtil;
import com.github.julyss2019.mcsp.julylibrary.utils.NMSUtil;
import com.github.julyss2019.mcsp.julylibrary.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class JulyMessage {
    private static Class<?> chatBaseComponentClass = null;
    private static Class<?> packetPlayOutTitleClass = null;
    private static Class<?> titleActionClass = null;
    private static Class<?> packetPlayOutChatClass = null;
    private static HashMap<String, String> prefixMap = new HashMap<>();

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

    public static String getPrefix(Plugin plugin) {
        return prefixMap.get(plugin.getClass().getPackage().getName());
    }

    /**
     *
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

    public static void sendBlankLine(CommandSender cs) {
        cs.sendMessage("");
    }

    public static void broadcastColoredMessage(String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendColoredMessage(player, msg);
        }
    }

    /**
     * 以某个插件的前缀发送信息
     * @param plugin 插件
     * @param cs
     * @param msg
     */
    public static void sendColoredMessage(Plugin plugin, CommandSender cs, String msg) {
        String prefix = getPrefix(plugin);

        sendColoredMessage(cs, prefix + msg, false);
    }

    public static void sendColoredMessage(CommandSender cs, String msg, boolean withPrefix) {
        if (!withPrefix) {
            cs.sendMessage(MessageUtil.translateColorCode(msg));
            return;
        }

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String prefix = "";

        for (Map.Entry<String, String> entry : prefixMap.entrySet()) {
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if (stackTraceElement.getClassName().startsWith(entry.getKey())) {
                    prefix = entry.getValue();
                    break;
                }
            }
        }

        cs.sendMessage(prefix + MessageUtil.translateColorCode(msg));
    }

    public static void sendColoredMessage(CommandSender cs, String msg) {
        sendColoredMessage(cs, msg, true);
    }

    public static boolean broadcastTitle(Title title) {
        if (!canUseTitle()) {
            throw new IllegalStateException("当前服务器版本不支持Title");
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!sendTitle(player, title)) {
                return false;
            }
        }

        return true;
    }

    public static boolean sendTitle(Player player, Title title) {
        if (!canUseTitle()) {
            throw new IllegalStateException("当前服务器版本不支持Title");
        }

        try {
            Object titleAction = titleActionClass.getField(title.getTitleType().name()).get(null); // 因为是 Enum 类，所以对象用 null
            Object chatBaseComponent = chatBaseComponentClass.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title.getText() + "\"}"); // 不需要对象，所以用 null
            // 调用构造方法，生成新的实例
            Object packet = packetPlayOutTitleClass.getDeclaredConstructor(titleActionClass, chatBaseComponentClass, int.class, int.class, int.class)
                    .newInstance(titleAction, chatBaseComponent, title.getFadeIn(), title.getStay(), title.getFadeOut());

            if (!PlayerUtil.sendPacket(player, packet)) {
                return false;
            }
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean canUseTitle() {
        return packetPlayOutTitleClass != null;
    }

    public static void setPrefix(JavaPlugin plugin, String prefix) {
        prefixMap.put(plugin.getClass().getPackage().getName(), prefix);
    }
}
