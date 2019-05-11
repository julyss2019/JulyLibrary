package com.github.julyss2019.mcsp.julylibrary.command;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public interface Command {
    boolean onCommand(CommandSender cs, String[] args);

    String getFirstArg();

    default boolean isOnlyPlayerCanUse() {
        return true;
    }

    default String getPermission() {
        return "";
    }

    default List<String> getDescriptions() {
        return Collections.emptyList();
    }
}
