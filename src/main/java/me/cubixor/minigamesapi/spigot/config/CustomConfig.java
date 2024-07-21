package me.cubixor.minigamesapi.spigot.config;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

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
