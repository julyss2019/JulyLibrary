package com.github.julyss2019.mcsp.julylibrary.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class YamlUtil {
    @Deprecated
    public static void saveYaml(YamlConfiguration yml, File file) {
        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static YamlConfiguration loadYaml(@NotNull File file, @NotNull Charset charset) {
        YamlConfiguration yaml = new YamlConfiguration();

        if (!file.exists()) {
            return yaml;
        }

        FileInputStream in = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;

        try {
            in = new FileInputStream(file);
            reader = new InputStreamReader(in, charset);
            bufferedReader = new BufferedReader(reader);

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

            yaml.loadFromString(sb.toString());
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return yaml;
    }

    /**
     * 使用特定的编码保存YAML
     * @param yml
     * @param file
     * @param charset
     */
    public static void saveYaml(@NotNull YamlConfiguration yml, @NotNull File file, @NotNull Charset charset) {
        File parentFile = file.getParentFile();

        if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
            throw new RuntimeException("创建父文件夹失败: " + parentFile.getAbsolutePath());
        }

        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new RuntimeException("创建文件失败: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("创建文件失败: " + file.getAbsolutePath(), e);
        }

        OutputStreamWriter writer = null;
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(file);
            writer = new OutputStreamWriter(out, charset);

            writer.write(yml.saveToString());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Location getLocationFromSection(ConfigurationSection section) {
        return new Location(Bukkit.getWorld(section.getString("world")), section.getDouble("x"), section.getDouble("y"), section.getDouble("z"), (float) section.getDouble("yaw"), (float) section.getDouble("pitch"));
    }

    @Deprecated
    public static Location getLocationBySection(@NotNull ConfigurationSection section) {
        return new Location(Bukkit.getWorld(section.getString("world")), section.getDouble("x"), section.getDouble("y"), section.getDouble("z"), (float) section.getDouble("yaw"), (float) section.getDouble("pitch"));
    }

    /**
     * 设置位置信息到 Section
     * @param section
     * @param location
     */
    public static void setLocationToSection(@NotNull ConfigurationSection section, @NotNull Location location) {
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
    }
}
