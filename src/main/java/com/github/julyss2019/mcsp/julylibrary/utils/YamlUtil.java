package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlUtil {
    private static class YamlException extends RuntimeException {
        public YamlException() {
        }
    }

    public static void saveYaml(YamlConfiguration yml, File file) {
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new YamlException();
        }
    }
}
