package me.cubixor.minigamesapi.spigot.game.arena.phases;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PhaseEnding extends GamePhase {

    private BukkitTask endingTask;

    public PhaseEnding(LocalArena localArena) {
        super(localArena);
    }

    @Override
    public void run() {
        localArena.setTimer(MinigamesAPI.getPlugin().getConfig().getInt("ending-time"));

        endingTask = new BukkitRunnable() {
            @Override
            public void run() {
                int time = localArena.getTimer();
                if (time <= 0) {
                    end();
                    return;
                }

                localArena.setTimer(time - 1);
            }

        }.runTaskTimer(MinigamesAPI.getPlugin(), 0, 20);
    }

    @Override
    public void stop() {
        endingTask.cancel();
        localArena.getStateManager().reset();
    }

    private void end() {
        stop();

        Set<Player> players = new HashSet<>(localArena.getBukkitPlayers());

        localArena.getStateManager().reset();
        localArena.getStateManager().autoJoin(players);
    }

    @Override
    public GameState getGameState() {
        return GameState.GAME;
    }


}
