package me.cubixor.minigamesapi.spigot.utils;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Location;
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
        XSound.Record soundRecord = XSound.parse(config.getString("sounds." + path));
        soundRecord.soundPlayer().atLocation(loc).forPlayers(players).play();
    }

    public static void playSoundWithPitch(String path, Player player, float pitch) {
        XSound.Record soundRecord = XSound
                .parse(config.getString("sounds." + path))
                .withPitch(pitch);
        soundRecord.soundPlayer().forPlayers(player).play();
    }
}
