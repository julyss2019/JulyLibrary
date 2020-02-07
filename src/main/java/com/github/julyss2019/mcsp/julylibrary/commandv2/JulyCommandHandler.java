package com.github.julyss2019.mcsp.julylibrary.commandv2;

import com.github.julyss2019.mcsp.julylibrary.commandv2.tab.Tab;
import com.github.julyss2019.mcsp.julylibrary.commandv2.tab.JulyTabHandler;
import com.github.julyss2019.mcsp.julylibrary.map.MapBuilder;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import com.github.julyss2019.mcsp.julylibrary.message.JulyText;
import com.github.julyss2019.mcsp.julylibrary.utils.ArrayUtil;
import com.github.julyss2019.mcsp.julylibrary.validate.NotNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

public class JulyCommandHandler extends JulyTabHandler implements CommandExecutor {
    private Map<String, JulyCommand> commandMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private Map<JulyCommand, Set<SubCommand>> subCommandMap = new HashMap<>();
    private String commandFormat = "/${cmd_name} ${args} ${desc}";

    /**
     * 设置命令有误时发送信息的格式
     * @param commandFormat
     */
    public void setCommandFormat(@NotNull String commandFormat) {
        this.commandFormat = commandFormat;
    }

    /**
     * 注册指令
     * @param julyCommand
     */
    public void registerCommand(@NotNull JulyCommand julyCommand) {
        if (commandMap.containsKey(julyCommand.getFirstArg())) {
            throw new RuntimeException("相同 Arg 的 JulyCommand 已注册");
        }

        subCommandMap.put(julyCommand, new HashSet<>());
        commandMap.put(julyCommand.getFirstArg(), julyCommand);

        Tab tab = new Tab(julyCommand.getFirstArg());

        for (Field field : julyCommand.getClass().getDeclaredFields()) {
            if (field.getAnnotation(SubCommandHandler.class) == null) {
                continue;
            }

            field.setAccessible(true);

            Object obj;

            try {
                obj = field.get(julyCommand);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (!(obj instanceof SubCommand)) {
                throw new RuntimeException("变量类型非 SubCommand");
            }

            SubCommand subCommand = (SubCommand) obj;

            if (subCommand.getDescription() == null) {
                throw new RuntimeException("SubCommand 中的 getDescription() 方法不能返回 null");
            }

            if (subCommand.getFirstArg() == null) {
                throw new RuntimeException("SubCommand 中的 getFirstArg() 方法不能返回 null");
            }

            if (subCommand.getLength() < 0) {
                throw new RuntimeException("SubCommand 中的 getLength() 方法必须大于等于0");
            }

            // 保存子命令
            subCommandMap.get(julyCommand).add(subCommand);
            // 注册 子Tab
            String firstArg = subCommand.getFirstArg();
            Tab[] tabs = subCommand.getTabs();

            if (firstArg != null && tabs != null) {
                tab.addSubTab(new Tab(firstArg).addSubTabs(tabs));
            }
        }

        addTab(tab);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        int argsLen = args.length;

        if (argsLen == 0 || !commandMap.containsKey(args[0])) {
            commandMap.values().forEach(julyCommand -> JulyMessage.sendColoredMessage(cs, JulyText.setPlaceholders(commandFormat, new MapBuilder<String, String>()
                    .put("cmd_name", cmd.getName())
                    .put("args", julyCommand.getFirstArg())
                    .put("desc", julyCommand.getDescription())
                    .build())));

            return true;
        }

        JulyCommand julyCommand = commandMap.get(args[0]);


        for (SubCommand subCommand : subCommandMap.get(julyCommand)) {
            int julySubCommandLen = subCommand.getLength();
            Predicate<CommandSender> usePredicate = subCommand.getUsePredicate();

            if ((usePredicate == null || usePredicate.test(cs))
                    && (julySubCommandLen == argsLen - 2 && args[1].equalsIgnoreCase(subCommand.getFirstArg()))) {
                subCommand.onCommand(cs, cmd, label, ArrayUtil.removeElementFromStrArray(ArrayUtil.removeElementFromStrArray(args, 0), 0));
                return true;
            }
        }

        for (SubCommand subCommand : subCommandMap.get(julyCommand)) {
            Predicate<CommandSender> usePredicate = subCommand.getUsePredicate();

            if (usePredicate == null || usePredicate.test(cs)) {
                JulyMessage.sendColoredMessage(cs, JulyText.setPlaceholders(commandFormat, new MapBuilder<String, String>()
                        .put("cmd_name", cmd.getName())
                        .put("args", julyCommand.getFirstArg() + " " + subCommand.getFirstArg())
                        .put("desc", subCommand.getDescription())
                        .build()));
            }
        }

        return true;
    }
}
