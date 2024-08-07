package me.cubixor.minigamesapi.spigot.game.arena.phases;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.stats.BasicStatsField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PhaseEnding extends GamePhase {

    private BukkitTask endingTask;
    private final ArenasManager arenasManager;
    private final StatsManager statsManager;
    private final List<Player> winners;

    public PhaseEnding(LocalArena localArena, ArenasManager arenasManager, StatsManager statsManager, List<Player> winners) {
        super(localArena);
        this.arenasManager = arenasManager;
        this.statsManager = statsManager;
        this.winners = winners;
    }

    @Override
    public void run() {
        localArena.setTimer(MinigamesAPI.getPlugin().getConfig().getInt("ending-time"));
        endGame();

        endingTask = new BukkitRunnable() {
            @Override
            public void run() {
                int time = localArena.getTimer();
                if (time <= 0) {
                    finish();
                    return;
                }

                localArena.getScoreboardManager().updateScoreboard();
                localArena.setTimer(time - 1);
            }

        }.runTaskTimer(MinigamesAPI.getPlugin(), 0, 20);
    }

    @Override
    public void stop() {
        endingTask.cancel();
    }

    private void endGame() {
        for (Player p : localArena.getBukkitPlayers()) {
            statsManager.addStats(p.getName(), BasicStatsField.PLAYTIME, 1);
            statsManager.addStats(p.getName(), BasicStatsField.GAMES, 1);
            if (winners.contains(p)) {
                statsManager.addStats(p.getName(), BasicStatsField.WINS, 1);
            } else {
                statsManager.addStats(p.getName(), BasicStatsField.LOOSES, 1);
            }
        }
    }

    private void finish() {
        Set<Player> players = new HashSet<>(localArena.getBukkitPlayers());
        localArena.getStateManager().reset();
        autoJoin(players);
    }

    @Override
    public GameState getGameState() {
        return GameState.ENDING;
    }

    private void autoJoin(Set<Player> players) {
        if (MinigamesAPI.getPlugin().getConfig().getBoolean("auto-join-on-end")) {
            for (Player p : players) {
                arenasManager.getArenaPlayersManager().joinRandomArena(p);
            }
        }
    }


}
