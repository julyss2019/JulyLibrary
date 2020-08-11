package com.github.julyss2019.mcsp.julylibrary.utils;

import com.github.julyss2019.mcsp.julylibrary.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class YamlUtil {
    /**
     * 从 ConfigurationSection 获得 ItemBuilder，支持：material，durability，display_name，lores，enchantments
     * @param section
     * @return
     */
    public static ItemBuilder getItemBuilder(@NotNull ConfigurationSection section) {
        ItemBuilder itemBuilder = new ItemBuilder()
                .colored()
                .material(section.getString("material"))
                .durability((short) section.getInt("durability"))
                .displayName(section.getString("display_name"))
                .lores(section.getStringList("lores"));

        if (section.contains("enchantments")) {
            for (String enchantmentName : section.getConfigurationSection("enchantments").getKeys(false)) {
                try {
                    itemBuilder.addEnchantment(Enchantment.getByName(enchantmentName), section.getInt("enchantments." + enchantmentName));
                } catch (Exception e) {
                    throw new RuntimeException("enchantments." + enchantmentName + " 附魔不合法", e);
                }
            }
        }

        return itemBuilder;
    }

    /**
     * 补全 Section
     * @param targetSection 目标 Section
     * @param completedSection 完整的 Section
     * @return
     */
    public static Set<String> completeSection(@NotNull ConfigurationSection targetSection, @NotNull ConfigurationSection completedSection) {
        return completeSection0(targetSection, completedSection, new HashSet<>());
    }

    private static Set<String> completeSection0(@NotNull ConfigurationSection targetSection, @NotNull ConfigurationSection completedSection, @NotNull Set<String> changes) {
        for (String key : completedSection.getKeys(false)) {
            // 如果是节点，则递归
            if (completedSection.isConfigurationSection(key)) {
                completeSection0(targetSection, completedSection.getConfigurationSection(key), changes);
            } else {
                String sectionPath = completedSection.getCurrentPath();
                String fullPath = sectionPath.equals("") ? key : sectionPath + "." + key;

                // 如果路径不存在或值为 null
                if (!targetSection.contains(fullPath) || targetSection.get(fullPath) == null) {
                    targetSection.set(fullPath, completedSection.get(key));
                    changes.add(fullPath);
                }
            }
        }

        return new HashSet<>(changes);
    }

    /**
     * 载入 Yaml
     * @param file
     * @param charset 编码
     * @return
     */
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

    /**
     * 从 ConfigurationSection 获取 Location
     * @param section
     * @return
     */
    public static Location getLocationFromSection(@NotNull ConfigurationSection section) {
        return new Location(Bukkit.getWorld(section.getString("world")), section.getDouble("x"), section.getDouble("y"), section.getDouble("z"), (float) section.getDouble("yaw"), (float) section.getDouble("pitch"));
    }

    /**
     * 设置 Location 到 ConfigurationSection
     * 用这个较为保险，低版本 Config 支持不好
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

    /**
     * 以默认编码保存 Yaml
     * @param yml
     * @param file
     */
    public static void saveYaml(@NotNull YamlConfiguration yml, @NotNull File file) {
        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public static Location getLocationBySection(@NotNull ConfigurationSection section) {
        return new Location(Bukkit.getWorld(section.getString("world")), section.getDouble("x"), section.getDouble("y"), section.getDouble("z"), (float) section.getDouble("yaw"), (float) section.getDouble("pitch"));
    }

    /**
     * 补全yaml
     * @param file 欲补全的文件
     * @param completedSection 完整的节点
     * @param charset 编码
     */
    @Deprecated
    public static void completeConfig(@NotNull File file, @NotNull ConfigurationSection completedSection, @NotNull Charset charset) {
        completeConfig0(file, loadYaml(file, charset), completedSection, charset);
    }

    @Deprecated
    private static void completeConfig0(@NotNull File targetFile, @NotNull YamlConfiguration targetYaml, @NotNull ConfigurationSection completedSection, @NotNull Charset charset) {
        boolean changed = false;

        for (String key : completedSection.getKeys(false)) {
            if (completedSection.isConfigurationSection(key)) {
                completeConfig0(targetFile, targetYaml, completedSection.getConfigurationSection(key), charset);
            } else {
                String sectionPath = completedSection.getCurrentPath();
                String fullPath = sectionPath.equals("") ? key : sectionPath + "." + key;

                if (!targetYaml.contains(fullPath)) {
                    targetYaml.set(fullPath, completedSection.get(key));
                    changed = true;
                }
            }
        }

        if (changed) {
            YamlUtil.saveYaml(targetYaml, targetFile, charset);
        }
    }
}
