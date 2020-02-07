package com.github.julyss2019.mcsp.julylibrary.commandv2;


import com.github.julyss2019.mcsp.julylibrary.commandv2.tab.Tab;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface SubCommand {
    @NotNull String getFirstArg();
    int getLength();
    @NotNull String getDescription();
    @Nullable Predicate<CommandSender> getUsePredicate();
    @Nullable Tab[] getTabs();
    void onCommand(CommandSender cs, Command cmd, String label, String[] args);
}
