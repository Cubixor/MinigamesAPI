package me.cubixor.minigamesapi.spigot.utils;

import com.cryptomorin.xseries.particles.XParticle;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Method;

public class Particles {

    private static FileConfiguration config;

    private Particles() {
    }

    public static void init(FileConfiguration config) {
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

    public static void spawnFirework(Location loc) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc.add(0, 1, 0), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.addEffect(FireworkEffect
                .builder()
                .withColor(Color.YELLOW)
                .with(FireworkEffect.Type.BALL)
                .flicker(true)
                .build());
        fwm.setPower(2);
        fw.setFireworkMeta(fwm);
    }

    public static void dropBlood(Location loc) {
        if (config.getBoolean("particles.enable-blood")) {
            try {
                Method createBlockData = Bukkit.class.getMethod("createBlockData", Material.class);
                loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 50, createBlockData.invoke(null, Material.REDSTONE_BLOCK));
            } catch (Error | Exception e) {
                loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 50, (Object) new MaterialData(Material.REDSTONE_BLOCK));
            }
        }
    }
}
