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
        String bukkitCommandName = bukkitCommand.getName();

        if (args.length >= 1) {
            String firstArg = args[0].toLowerCase();

            // 如果没有 help 命令，则按下面的实现进行
            if (firstArg.equalsIgnoreCase("help") && !commands.containsKey("help")) {
                for (JulyCommand command : commands.values()) {
                    String per = command.getPermission();

                    if (per == null || cs.hasPermission(per)) {
                        for (String desc : command.getDescriptions()) {
                            JulyMessage.sendColoredMessage(plugin, cs, "/" + bukkitCommandName + " " + command.getFirstArg() + " " + desc);
                        }
                    }

/*                    JulyMessage.sendColoredMessage(plugin, cs, "&c无权限!");
                    return true;*/
                }

                return true;
            }

            if (commands.containsKey(firstArg)) {
                JulyCommand command = commands.get(firstArg);

                if (command.isOnlyPlayerCanUse() && !(cs instanceof Player)) {
                    JulyMessage.sendColoredMessage(plugin, cs, "&c命令执行者必须是玩家!");
                    return true;
                }

                if (command.getPermission() != null && !cs.hasPermission(command.getPermission())) {
                    JulyMessage.sendColoredMessage(plugin, cs, "&c无权限!");
                    return true;
                }

                if (!command.onCommand(cs, ArrayUtil.removeElementFromStrArray(args, 0))) {
                    boolean messageSent = false;

                    // 匹配前缀
                    for (String desc : command.getDescriptions()) {
                        if (startsWithArgs(args[0] + " " + desc, args)) {
                            JulyMessage.sendColoredMessage(plugin, cs, "/" + bukkitCommandName + " " + command.getFirstArg() + " " + desc);
                            messageSent = true;
                        }
                    }

                    // 如果没有直接匹配到，逐次从尾删除参数直到匹配到为止
                    if (!messageSent && args.length > 1) {
                        onCommand(cs, bukkitCommand, label, ArrayUtil.removeElementFromStrArray(args, args.length - 1));
                    }
                }

                return true;
            }
        }

        Bukkit.dispatchCommand(cs, bukkitCommandName + " help");
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
}
