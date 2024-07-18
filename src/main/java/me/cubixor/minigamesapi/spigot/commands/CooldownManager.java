package me.cubixor.minigamesapi.spigot.commands;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class CooldownManager {

    private final MinigamesAPI plugin = MinigamesAPI.getInstance();
    private final Set<Player> cooldown = new HashSet<>();
    private final int cooldownTime;

    public CooldownManager() {
        cooldownTime = (int) Math.round(plugin.getConfig().getDouble("cooldown") * 20);
    }

    public void add(Player player) {
        if (cooldown.contains(player)) return;

        cooldown.add(player);

        Bukkit.getScheduler().runTaskLater(plugin, () -> cooldown.remove(player), cooldownTime);
    }

    public boolean check(Player player) {
        if (cooldown.contains(player)) {
            Messages.send(player, "general.too-fast");
            return true;
        }
        return false;
    }
}
