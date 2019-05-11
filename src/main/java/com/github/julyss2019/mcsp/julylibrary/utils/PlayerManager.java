package com.github.julyss2019.mcsp.julylibrary.utils;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerManager {
    public static void teleportSafely(Player player, Location location) {
        Bukkit.getScheduler().runTaskLater(JulyLibrary.getInstance(), () -> player.teleport(location), 5L);
    }
}
