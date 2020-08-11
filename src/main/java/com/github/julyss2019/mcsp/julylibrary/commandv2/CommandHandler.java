package com.github.julyss2019.mcsp.julylibrary.commandv2;

import com.github.julyss2019.mcsp.julylibrary.commandv2.registered.RegisteredCommand;
import com.github.julyss2019.mcsp.julylibrary.commandv2.registered.RegisteredSubCommand;
import com.github.julyss2019.mcsp.julylibrary.commandv2.tab.TabHandler;
import com.github.julyss2019.mcsp.julylibrary.commandv2.tab.Tab;
import com.github.julyss2019.mcsp.julylibrary.map.MapBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import com.github.julyss2019.mcsp.julylibrary.text.JulyText;
import com.github.julyss2019.mcsp.julylibrary.utils.ArrayUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommandHandler extends TabHandler implements CommandExecutor {
    private Map<String, RegisteredCommand> registeredCommandMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private String commandFormat = "/${label} ${arg} - ${desc}";
    private String subCommandFormat = "/${label} ${args} - ${desc}";
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
    public void setOnlyConsoleCanUseMessage(@NotNull String msg) {
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
        MainCommand mainCommand = julyCommand.getClass().getAnnotation(MainCommand.class);
        Class<?> clazz = julyCommand.getClass();

        if (mainCommand == null) {
            throw new RuntimeException(clazz.getName() + " 必须使用 @MainCommand 注解");
        }

        String firstArg = mainCommand.firstArg();

        if (registeredCommandMap.containsKey(firstArg)) {
            throw new RuntimeException("相同 arg 的类已注册");
        }

        RegisteredCommand registeredCommand = new RegisteredCommand();
        HashSet<RegisteredSubCommand> registeredSubCommands = new HashSet<>();
        Tab tab = new Tab(firstArg, new Predicate<CommandSender>() {
            @Override
            public boolean test(CommandSender sender) {
                String per = registeredCommand.getMainCommand().permission();

                return per.equals("") || sender.hasPermission(per);
            }
        });

        registeredCommand.setMainCommand(mainCommand);

        for (Method method : clazz.getDeclaredMethods()) {
            String methodName = method.getName();
            SubCommand subCommand = method.getAnnotation(SubCommand.class); // 注解

            if (subCommand == null) {
                continue;
            }

            method.setAccessible(true);

            if (method.getReturnType() != void.class) {
                throw new RuntimeException(methodName + " 方法返回类型必须为 void");
            }

            Class<?>[] parameters = method.getParameterTypes();

            if (parameters.length != 2 || parameters[0] != CommandSender.class || parameters[1] != String[].class) {
                throw new RuntimeException(methodName + " 方法参数必须为 CommandSender, String[]");
            }

            if (subCommand.description().equals("")) {
                throw new RuntimeException(methodName + " 方法 @SubCommand 的 description() 不能返回空");
            }

            if (subCommand.firstArg().equals("")) {
                throw new RuntimeException(methodName + " 方法 @SubCommand 的 firstArg() 不能返回空");
            }

            int len = subCommand.length();

            if (len < -1) {
                throw new RuntimeException(methodName + " 方法 @SubCommand 的 length() 必须 >= -1");
            }

            if (len != -1 && subCommand.subArgs().length != len) {
                throw new RuntimeException(methodName + " 方法 @SubCommand 的 length() 与 args() 不匹配");
            }

            if (subCommand.senders().length == 0) {
                throw new RuntimeException(methodName + " 方法 @SubCommand 的 senders() 必须填写至少一个成员");
            }

            registeredSubCommands.add(new RegisteredSubCommand(registeredCommand, subCommand, julyCommand, method));
            // 添加sub
            tab.addSubTab(new Tab(subCommand.firstArg(), new Predicate<CommandSender>() {
                @Override
                public boolean test(CommandSender sender) {
                    String per = registeredCommand.getMainCommand().permission();

                    return per.equals("") || sender.hasPermission(per);
                }
            }));
        }

        registeredCommand.setRegisteredSubCommands(registeredSubCommands);
        registeredCommandMap.put(firstArg, registeredCommand);
        addTab(tab);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        int argsLen = args.length;

        if (argsLen == 0 || !registeredCommandMap.containsKey(args[0])) {
            boolean sentOne = false;

            out:
            for (RegisteredCommand registeredCommand : registeredCommandMap.values()
                    .stream().sorted((o1, o2) -> o2.getMainCommand().priority() - o1.getMainCommand().priority())
                    .collect(Collectors.toList())) {
                MainCommand mainCommand = registeredCommand.getMainCommand();

                for (RegisteredSubCommand registeredSubCommand : registeredCommand.getRegisteredSubCommands()) {
                    SubCommand subCommand = registeredSubCommand.getSubCommand();

                    if (senderEquals(cs, subCommand.senders()) && (subCommand.permission().equals("") || cs.hasPermission(subCommand.permission()))) {
                        JulyMessage.sendColoredMessage(cs, JulyText.setPlaceholders(commandFormat, new MapBuilder<String, String>()
                                .put("label", label)
                                .put("arg", mainCommand.firstArg())
                                .put("desc", mainCommand.description())
                                .build()));
                        sentOne = true;
                        continue out;
                    }
                }
            }

            if (!sentOne) {
                JulyMessage.sendColoredMessage(cs, noneMessage);
            }

            return true;
        }

        RegisteredCommand registeredCommand = registeredCommandMap.get(args[0]);
        String mainCommandPer = registeredCommand.getMainCommand().permission();

        if (!mainCommandPer.equals("") && !cs.hasPermission(mainCommandPer)) {
            JulyMessage.sendColoredMessage(cs, JulyText.setPlaceholders(noPermissionMessage,
                    new MapBuilder<String, String>().put("per", mainCommandPer).build()));
            return true;
        }

        if (argsLen > 1) {
            for (RegisteredSubCommand registeredSubCommand : registeredCommand.getRegisteredSubCommands()) {
                SubCommand subCommand = registeredSubCommand.getSubCommand();
                int subArgsLen = subCommand.length();

                if (args[1].equalsIgnoreCase(subCommand.firstArg()) && (subArgsLen == -1 || (subArgsLen == argsLen - 2))) {
                    if (!senderEquals(cs, subCommand.senders())) {
                        JulyMessage.sendColoredMessage(cs, !(cs instanceof Player) ? onlyPlayerCanUseMessage : onlyConsoleCanUseMessage);
                        return true;
                    }

                    String per = subCommand.permission();

                    // 权限判断
                    if (!per.equals("") && !cs.hasPermission(per)) {
                        JulyMessage.sendColoredMessage(cs, JulyText.setPlaceholders(noPermissionMessage,
                                new MapBuilder<String, String>().put("per", per).build()));
                        return true;
                    }

                    try {
                        registeredSubCommand.execute(cs, ArrayUtil.removeElementFromStrArray(ArrayUtil.removeElementFromStrArray(args, 0), 0));
                    } catch (IllegalAccessError | InvocationTargetException | IllegalAccessException ex) {
                        throw new RuntimeException("执行命令时发生了错误", ex);
                    } catch (InvalidArgumentException invalidArgumentException) {
                        sendSubCommandUsage(cs, label, registeredSubCommand);
                        return true;
                    }

                    return true;
                }
            }
        }

        boolean sentOne = false;

        for (RegisteredSubCommand registeredSubCommand : registeredCommand.getRegisteredSubCommands().stream()
                .sorted((o1, o2) -> o2.getSubCommand().priority() - o1.getSubCommand().priority())
                .collect(Collectors.toList())) {
            SubCommand subCommand = registeredSubCommand.getSubCommand();

            if (!senderEquals(cs, subCommand.senders())) {
                continue;
            }

            String per = subCommand.permission();

            if (!per.equals("") && !cs.hasPermission(per)) {
                continue;
            }

            sendSubCommandUsage(cs, label, registeredSubCommand);
            sentOne = true;
        }

        if (!sentOne) {
            JulyMessage.sendColoredMessage(cs, noneMessage);
        }

        return true;
    }

    private void sendSubCommandUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull RegisteredSubCommand registeredSubCommand) {
        SubCommand subCommand = registeredSubCommand.getSubCommand();
        String[] subArgs = subCommand.subArgs();

        JulyMessage.sendColoredMessage(sender, JulyText.setPlaceholders(subCommandFormat, new MapBuilder<String, String>()
                .put("label", label)
                .put("args", registeredSubCommand.getRegisteredCommand().getMainCommand().firstArg() + " " + subCommand.firstArg() + (subArgs.length == 0 ? "" : " " + argsToStr(subCommand.subArgs())))
                .put("desc", subCommand.description())
                .build()));
    }

    /**
     * 能否使用指令
     * @param cs
     * @param senderTypes 允许使用的身份（多个）
     * @return
     */
    private static boolean senderEquals(@NotNull CommandSender cs, @NotNull SenderType... senderTypes) {
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
