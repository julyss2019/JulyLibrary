package com.github.julyss2019.mcsp.julylibrary.command;

import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import com.github.julyss2019.mcsp.julylibrary.utils.ArrayUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class JulyCommandExecutor implements org.bukkit.command.CommandExecutor {
    private Map<String, JulyCommand> commands = new HashMap<>();
    private Plugin plugin;

    public JulyCommandExecutor(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 注册指令
     * @param command
     */
    public void register(JulyCommand command) {
        commands.put(command.getFirstArg().toLowerCase(), command);
    }

    @Override
    public boolean onCommand(CommandSender cs, org.bukkit.command.Command bukkitCommand, String label, String[] args) {
        if (args.length > 0) {
            String firstArg = args[0].toLowerCase();

            // 如果存在命令
            if (commands.containsKey(firstArg)) {
                JulyCommand command = commands.get(firstArg);

                if (command.isOnlyPlayerCanUse() && !(cs instanceof Player)) {
                    sendMessage(plugin, cs, "&c命令执行者必须是玩家!");
                    return true;
                }

                if (command.getPermission() != null && !cs.hasPermission(command.getPermission())) {
                    sendMessage(plugin, cs, "&c无权限!");
                    return true;
                }

                // 没有执行成功
                if (!command.onCommand(cs, ArrayUtil.removeElementFromStrArray(args, 0))) {
                    boolean messageSent = false;

                    // 匹配前缀
                    for (String desc : command.getSubDescriptions()) {
                        if (startsWithArgs(args[0] + " " + desc, args)) {
                            sendMessage(plugin, cs, "/" + label + " " + command.getFirstArg() + " " + desc);
                            messageSent = true;
                        }
                    }

                    // 如果没有直接匹配到，逐次从尾删除参数直到匹配到为止
                    if (!messageSent) {
                        onCommand(cs, bukkitCommand, label, ArrayUtil.removeElementFromStrArray(args, args.length - 1));
                    }
                }

                return true;
            }

            // 如果没有 help 命令，则按下面的实现进行
            if (firstArg.equalsIgnoreCase("help")) {
                for (JulyCommand command : commands.values()) {
                    String per = command.getPermission();
                    String desc = command.getDescription();

                    if (cs.hasPermission(per) && desc != null) {
                        sendMessage(plugin, cs, "/" + label + " " + command.getFirstArg() + " - " + desc);
                    }
                }

                return true;
            }
        }

        Bukkit.dispatchCommand(cs, label + " help");
        return true;
    }

    /**
     * 是否以指定参数开始的文本
     * @param s
     * @param args
     * @return
     */
    private boolean startsWithArgs(String s, String[] args) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);

            if (i != args.length - 1) {
                sb.append(" ");
            }
        }

        return s.startsWith(sb.toString());
    }

    /**
     * 以某个插件的前缀发送消息（带前缀）
     * @param plugin 插件
     * @param cs
     * @param msg
     */
    private void sendMessage(Plugin plugin, CommandSender cs, String msg) {
        String tmp = JulyMessage.getPrefix(plugin);

        JulyMessage.sendColoredMessage(cs, (tmp == null ? "" : tmp) + msg, false);
    }
}
