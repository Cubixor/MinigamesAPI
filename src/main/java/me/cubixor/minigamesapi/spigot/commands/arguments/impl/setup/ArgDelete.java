package me.cubixor.minigamesapi.spigot.commands.arguments.impl.setup;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.commands.arguments.ArenaCommandArgument;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ArgDelete extends ArenaCommandArgument {

    private final Map<Player, String> awaitingConfirm = new HashMap<>();

    public ArgDelete(ArenasManager arenasManager) {
        super(arenasManager, "delete", "setup.delete", 2, "arena-setup.delete", true, false);
    }

    @Override
    public void handle(Player player, String[] args) {
        String arenaName = args[1];
        if (awaitingConfirm.containsKey(player) && awaitingConfirm.get(player).equalsIgnoreCase(arenaName)) {
            handleConfirm(player, arenaName);
            return;
        }

        awaitingConfirm.put(player, arenaName);
        confirmScheduler(player);
        Messages.send(player, "arena-setup.delete-confirm", "%arena%", arenaName);
    }

    private void handleConfirm(Player player, String arenaName) {
        arenasManager.removeArena(arenaName);
        Messages.send(player, "arena-setup.delete-success", "%arena%", arenaName);
    }

    public void confirmScheduler(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                awaitingConfirm.remove(player);
            }
        }.runTaskLater(MinigamesAPI.getPlugin(), 400);
    }
}
