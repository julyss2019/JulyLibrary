package com.github.julyss2019.mcsp.julylibrary.commandv2;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public enum SenderType {
    PLAYER, CONSOLE;

    boolean canUse(@NotNull CommandSender sender) {
        if (this == PLAYER && sender instanceof Player) {
            return true;
        }

        return this == CONSOLE && sender instanceof ConsoleCommandSender;
    }
}
