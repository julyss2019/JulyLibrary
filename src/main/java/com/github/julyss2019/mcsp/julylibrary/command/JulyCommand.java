package com.github.julyss2019.mcsp.julylibrary.command;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@Deprecated
public interface JulyCommand {
    boolean onCommand(CommandSender cs, String[] args);

    String getFirstArg();

    default boolean isOnlyPlayerCanUse() {
        return true;
    }

    default String getPermission() {
        return null;
    }

    default List<String> getSubDescriptions() {
        return Collections.emptyList();
    }

    default String getDescription() { return null; }
}
