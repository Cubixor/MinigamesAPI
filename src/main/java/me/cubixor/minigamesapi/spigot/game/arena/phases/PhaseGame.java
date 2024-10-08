package me.cubixor.minigamesapi.spigot.game.arena.phases;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.utils.Sounds;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class PhaseGame extends GamePhase {

    private BukkitTask gameTask;

    public PhaseGame(LocalArena localArena) {
        super(localArena);
    }

    @Override
    public void run() {
        start();

        gameTask = new BukkitRunnable() {
            @Override
            public void run() {
                int time = localArena.getTimer();

                localArena.setTimer(time + 1);

                localArena.getScoreboardManager().updateScoreboard();
            }

        }.runTaskTimer(MinigamesAPI.getPlugin(), 0, 20);
    }

    @Override
    public void stop() {
        gameTask.cancel();
    }

    private void start() {
        localArena.setTimer(0);

        for (Player p : localArena.getBukkitPlayers()) {
            p.getInventory().clear();

            Sounds.playSound("start", p);
        }
    }

    public void finish(List<Player> winners) {
        localArena.getStateManager().setEnd(winners);
    }


    @Override
    public GameState getGameState() {
        return GameState.GAME;
    }


}
