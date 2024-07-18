package me.cubixor.minigamesapi.spigot.utils;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class Sounds {

    private static FileConfiguration config;

    public Sounds(FileConfiguration config) {
        Sounds.config = config;
    }

    public static void playSound(String path, Location loc, Set<Player> players) {
        //TODO Edit format in config
        XSound.Record soundRecord = XSound.parse(config.getString("sounds." + path));
        soundRecord.soundPlayer().atLocation(loc).forPlayers(players).play();
    }
}
