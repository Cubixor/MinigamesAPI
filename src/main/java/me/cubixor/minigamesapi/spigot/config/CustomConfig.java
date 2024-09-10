package me.cubixor.minigamesapi.spigot.config;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CustomConfig {

    private final JavaPlugin plugin = MinigamesAPI.getPlugin();
    private final File file;
    private final FileConfiguration fileConfiguration;


    public CustomConfig(String name) {
        file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, false);
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void copyDefaults() {
        final InputStream defConfigStream = plugin.getResource(file.getName());
        FileConfiguration defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));

        boolean versionMismatch = fileConfiguration.getDouble("config-version") != defaults.getDouble("config-version");

        fileConfiguration.setDefaults(defaults);
        fileConfiguration.options().copyDefaults(true);

        if (versionMismatch) {
            fileConfiguration.set("config-version", defaults.getDouble("config-version"));
            save();
        }
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration get() {
        return fileConfiguration;
    }
}
