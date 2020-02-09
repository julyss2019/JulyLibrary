package com.github.julyss2019.mcsp.julylibrary.commandv2;

import com.github.julyss2019.mcsp.julylibrary.validate.NotNull;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public enum SenderType {
    PLAYER, CONSOLE;

    boolean canUse(@NotNull CommandSender sender) {
        if (this == PLAYER && sender instanceof Player) {
            return true;
        }

        return this == CONSOLE && sender instanceof ConsoleCommandSender;
    }
}
