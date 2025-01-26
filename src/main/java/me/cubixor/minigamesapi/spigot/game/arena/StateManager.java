package me.cubixor.minigamesapi.spigot.game.arena;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.events.GameEndEvent;
import me.cubixor.minigamesapi.spigot.events.GameResetEvent;
import me.cubixor.minigamesapi.spigot.events.GameStartEvent;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.phases.*;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class StateManager {

    private final JavaPlugin plugin;
    private final LocalArena localArena;
    private final ArenasManager arenasManager;
    private final StatsManager statsManager;

    private GamePhase gamePhase;

    public StateManager(LocalArena localArena, ArenasManager arenasManager, StatsManager statsManager) {
        this.localArena = localArena;
        this.arenasManager = arenasManager;
        this.statsManager = statsManager;
        this.plugin = MinigamesAPI.getPlugin();

        this.gamePhase = new PhaseWaiting(localArena);
    }

    private void updatePhase(GamePhase phase) {
        gamePhase.stop();
        localArena.setState(phase.getGameState());
        gamePhase = phase;
        gamePhase.run();
        arenasManager.updateArena(localArena);
        localArena.getScoreboardManager().updateScoreboard();
    }

    public void updateOnJoin() {
        int count = localArena.getPlayers().size();
        int min = localArena.getMinPlayers();
        int max = localArena.getMaxPlayers();

        if (count >= min) {
            if (gamePhase instanceof PhaseWaiting) {
                setStarting();
            }

            if (count >= max) {
                setFullStarting();
                for (Player p : localArena.getBukkitPlayers()) {
                    Messages.send(p, "game.full-countdown", "%time%", String.valueOf(localArena.getTimer()));
                }
            }
        }

        localArena.getScoreboardManager().updateScoreboard();
    }

    public void updateOnLeave() {
        int count = localArena.getPlayers().size();
        if (localArena.getState() == GameState.STARTING) {
            int min = localArena.getMinPlayers();

            if (count < min) {
                for (Player p : localArena.getBukkitPlayers()) {
                    Messages.send(p, "game.start-cancelled");
                }

                setWaiting();
            }
        } else if (localArena.getState() == GameState.GAME && count < 2) {
            if (count == 1) {
                Messages.send(localArena.getBukkitPlayers().stream().findFirst().get(),
                        "game.stopped-one-player");
            }
            reset();
        }

        localArena.getScoreboardManager().updateScoreboard();
    }

    public void setWaiting() {
        updatePhase(new PhaseWaiting(localArena));
    }

    public void setStarting() {
        updatePhase(new PhaseStarting(localArena));
    }

    public void setFullStarting() {
        localArena.setTimer(plugin.getConfig().getInt("full-waiting-time"));
    }

    public void setGame() {
        updatePhase(new PhaseGame(localArena));

        Bukkit.getPluginManager().callEvent(new GameStartEvent(localArena));
    }

    public void setEnd(List<Player> winners) {
        PhaseEnding phaseEnding = new PhaseEnding(localArena, arenasManager, statsManager, winners);
        updatePhase(phaseEnding);

        Bukkit.getPluginManager().callEvent(new GameEndEvent(localArena, winners, phaseEnding.getGameTime()));
    }


    public void reset() {
        if (gamePhase != null) {
            gamePhase.stop();
        }

        localArena.setState(GameState.ENDING);

        List<Player> players = new ArrayList<>(localArena.getBukkitPlayers());
        for (Player p : players) {
            arenasManager.getArenaPlayersManager().kickFromLocalArena(p, localArena, true);
        }
        localArena.cancelTasks();

        GameResetEvent gameResetEvent = new GameResetEvent(localArena);
        Bukkit.getPluginManager().callEvent(gameResetEvent);

        if (gameResetEvent.isResetFinished()) {
            setWaiting();
        }
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }
}
