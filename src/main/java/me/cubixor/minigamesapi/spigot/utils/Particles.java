package me.cubixor.minigamesapi.spigot.utils;

import com.cryptomorin.xseries.particles.XParticle;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Particles {

    private static FileConfiguration config;

    public Particles(FileConfiguration config) {
        Particles.config = config;
    }


    public static void spawnParticle(Location loc, String path) {
        if (!config.getBoolean("particles." + path + ".enabled")) {
            return;
        }

        String particleString = config.getString("particles." + path + ".particle");
        int count = config.getInt("particles." + path + ".count");
        float offset = (float) config.getDouble("particles." + path + ".offset");
        float speed = (float) config.getDouble("particles." + path + ".speed");

        loc.getWorld().spawnParticle(XParticle.of(particleString).get().get(), loc, count, offset, offset, offset, speed);
    }
}
