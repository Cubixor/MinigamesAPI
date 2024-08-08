package me.cubixor.minigamesapi.spigot.utils;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class Sounds {

    private static FileConfiguration config;

    private Sounds() {
    }

    public static void init(FileConfiguration config) {
        Sounds.config = config;
    }

    public static void playSound(String path, Location loc, Set<Player> players) {
        if (!config.getBoolean("sounds." + path + ".enabled")) {
            return;
        }

        Sound sound = XSound.matchXSound(config.getString("sounds." + path + ".sound")).get().parseSound();
        float volume = (float) config.getDouble("sounds." + path + ".volume");
        float pitch = (float) config.getDouble("sounds." + path + ".pitch");

        players.forEach(p -> p.playSound(loc, sound, volume, pitch));

    }

    public static void playSoundWithPitch(String path, Player p, float pitch) {
        if (!config.getBoolean("sounds." + path + ".enabled")) {
            return;
        }

        Sound sound = XSound.matchXSound(config.getString("sounds." + path + ".sound")).get().parseSound();
        float volume = (float) config.getDouble("sounds." + path + ".volume");

        p.playSound(p.getLocation(), sound, volume, pitch);
    }

}
