package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlUtil {
    public static boolean saveYaml(YamlConfiguration yml, File file) {
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
