package com.github.julyss2019.mcsp.julylibrary.commandv2;

import com.github.julyss2019.mcsp.julylibrary.commandv2.registered.RegisteredCommand;
import com.github.julyss2019.mcsp.julylibrary.commandv2.registered.RegisteredSubCommand;
import com.github.julyss2019.mcsp.julylibrary.commandv2.tab.JulyTabHandler;
import com.github.julyss2019.mcsp.julylibrary.commandv2.tab.Tab;
import com.github.julyss2019.mcsp.julylibrary.map.MapBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import com.github.julyss2019.mcsp.julylibrary.message.JulyText;
import com.github.julyss2019.mcsp.julylibrary.utils.ArrayUtil;
import com.github.julyss2019.mcsp.julylibrary.validate.NotNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.*;

public class JulyCommandHandler extends JulyTabHandler implements CommandExecutor {
    private Map<String, RegisteredCommand> registeredCommandMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private String commandFormat = "/${cmd_name} ${arg} - ${desc}";
    private String subCommandFormat = "/${cmd_name} ${args} - ${desc}";
    private String noPermissionMessage = "&c你没有权限: ${per}.";
    private String onlyPlayerCanUseMessage = "&c该命令只能由玩家执行.";
    private String onlyConsoleCanUseMessage = "&c该命令只能由控制台执行.";
    private String noneMessage = "&c没有可供显示的内容.";

    public String getNoneMessage() {
        return noneMessage;
    }

    /**
     * 设置无内容提示信息
     * @param noneMessage
     */
    public void setNoneMessage(@NotNull String noneMessage) {
        this.noneMessage = noneMessage;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    /**
     * 设置无权限提示信息
     * @param msg 变量：${per}
     */
    public void setNoPermissionMessage(@NotNull String msg) {
        this.noPermissionMessage = msg;
    }

    public String getOnlyPlayerCanUseMessage() {
        return onlyPlayerCanUseMessage;
    }

    /**
     * 设置只能由玩家执行信息
     * @param msg 变量：${cmd}
     * @return
     */
    public void setOnlyPlayerCanUseMessage(@NotNull String msg) {
        this.onlyPlayerCanUseMessage = msg;
    }

    public String getOnlyConsoleCanUseMessage() {
        return onlyConsoleCanUseMessage;
    }

    /**
     * 设置只能由控制台执行信息
     * @param msg 变量：${cmd}
     * @return
     */
    public void setOnlyConsoleCanUseMessage(String msg) {
        this.onlyConsoleCanUseMessage = msg;
    }

    public String getCommandFormat() {
        return commandFormat;
    }

    /**
     * 设置主命令有误时发送信息的格式
     * @param format 格式，变量：${cmd_name} 命令 ${arg} 参数 ${desc} 介绍
     */
    public void setCommandFormat(@NotNull String format) {
        this.commandFormat = format;
    }

    public String getSubCommandFormat() {
        return subCommandFormat;
    }

    /**
     * 设置子命令有误时发送信息的格式
     * @param format 格式，变量：${cmd_name} 命令 ${args} 参数(多个) ${desc} 介绍
     */
    public void setSubCommandFormat(@NotNull String format) {
        this.subCommandFormat = format;
    }


    /**
     * 注册指令
     * @param julyCommand
     */
    public void registerCommand(@NotNull JulyCommand julyCommand) {
        if (registeredCommandMap.containsKey(julyCommand.getFirstArg())) {
            throw new RuntimeException("相同 Arg 的 JulyCommand 已注册");
        }

        HashSet<RegisteredSubCommand> registeredSubCommands = new HashSet<>();
        String firstArg = julyCommand.getFirstArg();
        Tab tab = new Tab(firstArg);

        for (Method method : julyCommand.getClass().getDeclaredMethods()) {
            SubCommandHandler subCommandHandler = method.getAnnotation(SubCommandHandler.class); // 注解

            if (subCommandHandler == null) {
                continue;
            }

            method.setAccessible(true);

            if (subCommandHandler.description() == null) {
                throw new RuntimeException("SubCommandHandler 中的 description() 不能返回 null");
            }

            if (subCommandHandler.firstArg() == null) {
                throw new RuntimeException("SubCommandHandler 中的 firstArg() 不能返回 null");
            }

            if (subCommandHandler.length() < 0) {
                throw new RuntimeException("SubCommandHandler 中的 length() 必须>=0");
            }

            if (subCommandHandler.senders().length == 0) {
                throw new RuntimeException("SubCommandHandler 中的 senders() 必须填写至少一个成员");
            }

            if (subCommandHandler.length() != subCommandHandler.subArgs().length) {
                throw new RuntimeException("SubCommandHandler 中的 length() 与 subArgs() 长度不一致");
            }

            // 保存子命令
            registeredSubCommands.add(new RegisteredSubCommand(julyCommand, subCommandHandler, method));
            // 添加sub
            tab.addSubTab(new Tab(subCommandHandler.firstArg()));
        }

        registeredCommandMap.put(firstArg, new RegisteredCommand(julyCommand, registeredSubCommands));
        addTab(tab);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        int argsLen = args.length;

        if (argsLen == 0 || !registeredCommandMap.containsKey(args[0])) {
            out:
            for (RegisteredCommand registeredCommand : registeredCommandMap.values()) {
                JulyCommand julyCommand = registeredCommand.getJulyCommand();

                for (RegisteredSubCommand registeredSubCommand : registeredCommand.getRegisteredSubCommands()) {
                    SubCommandHandler subCommandHandler = registeredSubCommand.getSubCommandHandler();

                    if (canUse(cs, subCommandHandler.senders()) && (subCommandHandler.permission().equals("") || cs.hasPermission(subCommandHandler.permission()))) {
                        JulyMessage.sendColoredMessage(cs, JulyText.setPlaceholders(commandFormat, new MapBuilder<String, String>()
                                .put("cmd_name", cmd.getName())
                                .put("arg", julyCommand.getFirstArg())
                                .put("desc", julyCommand.getDescription())
                                .build()));
                        continue out;
                    }
                }
            }

            return true;
        }

        RegisteredCommand registeredCommand = registeredCommandMap.get(args[0]);

        for (RegisteredSubCommand registeredSubCommand : registeredCommand.getRegisteredSubCommands()) {
            SubCommandHandler subCommandHandler = registeredSubCommand.getSubCommandHandler();
            int subArgsLen = subCommandHandler.length();

            if (subArgsLen == argsLen - 2 && args[1].equalsIgnoreCase(subCommandHandler.firstArg())) {
                if (!canUse(cs, subCommandHandler.senders())) {
                    JulyMessage.sendColoredMessage(cs, cs instanceof Player ? onlyPlayerCanUseMessage : onlyConsoleCanUseMessage);
                    return true;
                }

                String per = subCommandHandler.permission();

                // 权限判断
                if (per != null && !cs.hasPermission(per)) {
                    JulyMessage.sendColoredMessage(cs, JulyText.setPlaceholders(noPermissionMessage,
                            new MapBuilder<String, String>().put("per", per).build()));
                    return true;
                }

                registeredSubCommand.execute(cs, ArrayUtil.removeElementFromStrArray(ArrayUtil.removeElementFromStrArray(args, 0), 0));
                return true;
            }
        }

        for (RegisteredSubCommand registeredSubCommand : registeredCommand.getRegisteredSubCommands()) {
            SubCommandHandler subCommandHandler = registeredSubCommand.getSubCommandHandler();

            if (!canUse(cs, subCommandHandler.senders())) {
                continue;
            }

            if (!subCommandHandler.permission().equals("") && !cs.hasPermission(subCommandHandler.permission())) {
                continue;
            }

            String[] subArgs = subCommandHandler.subArgs();

            JulyMessage.sendColoredMessage(cs, JulyText.setPlaceholders(subCommandFormat, new MapBuilder<String, String>()
                    .put("cmd_name", cmd.getName())
                    .put("args", registeredSubCommand.getJulyCommand().getFirstArg() + " " + subCommandHandler.firstArg() + (subArgs.length == 0 ? "" : " " + argsToStr(subCommandHandler.subArgs())))
                    .put("desc", subCommandHandler.description())
                    .build()));
            return true;
        }

        JulyMessage.sendColoredMessage(cs, noneMessage);
        return true;
    }

    /**
     * 能否使用指令
     * @param cs
     * @param senderTypes 允许使用的身份（多个）
     * @return
     */
    private static boolean canUse(@NotNull CommandSender cs, @NotNull SenderType... senderTypes) {
        for (SenderType senderType : senderTypes) {
            if (senderType.canUse(cs)) {
                return true;
            }
        }

        return false;
    }

    private static String argsToStr(@NotNull String[] args) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            stringBuilder.append(args[i]);

            if (i != args.length - 1) {
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();
    }
}
