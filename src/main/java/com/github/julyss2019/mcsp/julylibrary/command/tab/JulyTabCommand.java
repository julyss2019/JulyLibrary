package com.github.julyss2019.mcsp.julylibrary.command.tab;

import com.github.julyss2019.mcsp.julylibrary.command.JulyCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JulyTabCommand extends JulyCommand {
    TabCompleter getTabCompleter();

    /**
     * 在 JulyTabCompleter 处理 TabCompleter 前触发，并将其返回值追加到最后结果中，主要用于处理未知的 Tab。
     * @param cs
     * @param command
     * @param label
     * @param args
     * @return
     */
    default List<String> onTabComplete(CommandSender cs, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}
