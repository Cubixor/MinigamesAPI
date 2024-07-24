package me.cubixor.minigamesapi.spigot.game.arena;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.events.GameEndEvent;
import me.cubixor.minigamesapi.spigot.events.GameStartEvent;
import me.cubixor.minigamesapi.spigot.game.ArenaPlayersManager;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.game.arena.phases.GamePhase;
import me.cubixor.minigamesapi.spigot.game.arena.phases.PhaseEnding;
import me.cubixor.minigamesapi.spigot.game.arena.phases.PhaseGame;
import me.cubixor.minigamesapi.spigot.game.arena.phases.PhaseStarting;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StateManager {

    private final JavaPlugin plugin;
    private final LocalArena localArena;
    private final ArenaPlayersManager arenaPlayersManager;

    private GamePhase gamePhase;

    public StateManager(LocalArena localArena, ArenaPlayersManager arenaPlayersManager) {
        this.localArena = localArena;
        this.arenaPlayersManager = arenaPlayersManager;
        this.plugin = MinigamesAPI.getPlugin();
    }

    private void updatePhase(GamePhase phase) {
        localArena.setState(phase.getGameState());
        gamePhase = phase;
        gamePhase.run();
    }

    public void updateOnJoin() {
        int count = localArena.getPlayers().size();
        int min = localArena.getMinPlayers();

        if (count >= min) {
            setStarting();
        }
    }

    public void updateOnLeave() {
        int count = localArena.getPlayers().size();
        if (localArena.getState() == GameState.STARTING) {
            int min = localArena.getMinPlayers();

            if (count < min) {
                setWaiting();
            }
        } else if (localArena.getState() == GameState.GAME && count < 2) {
                if (count == 1) {
                    Messages.send(localArena.getBukkitPlayers().stream().findFirst().get(),
                            "game.stopped-one-player");
                }
                reset();
            }

    }

    private void setWaiting() {
        gamePhase.stop();

        for (Player p : localArena.getBukkitPlayers()) {
            Messages.send(p, "game.start-cancelled");
            //TODO Send action bar
        }
        localArena.setTimer(-1);
    }

    private void setStarting() {
        if (localArena.getTimer() == -1) {
            updatePhase(new PhaseStarting(localArena));
        }

        int count = localArena.getPlayers().size();
        int max = localArena.getMaxPlayers();

        if (count >= max) {
            localArena.setTimer(plugin.getConfig().getInt("full-tp-waiting-time"));
            for (Player p : localArena.getBukkitPlayers()) {
                Messages.send(p, "game.full-countdown", "%time%", String.valueOf(localArena.getTimer()));
            }
        }
    }

    public void setGame() {
        Bukkit.getPluginManager().callEvent(new GameStartEvent(localArena));

        updatePhase(new PhaseGame(localArena));
    }

    public void setEnd(){
        Bukkit.getPluginManager().callEvent(new GameEndEvent(localArena));

        updatePhase(new PhaseEnding(localArena));
    }


    public void reset() {
        if(gamePhase != null){
            gamePhase.stop();
        }

        localArena.setState(GameState.ENDING);

        List<Player> players = new ArrayList<>(localArena.getBukkitPlayers());
        for (Player p : players) {
            arenaPlayersManager.kickFromLocalArena(p, localArena, true);
        }

        localArena.setState(GameState.WAITING);
        localArena.setTimer(-1);
    }

    public void autoJoin(Set<Player> players){
        if (MinigamesAPI.getPlugin().getConfig().getBoolean("auto-join-on-end")) {
            for (Player p : players) {
                arenaPlayersManager.joinRandomArena(p);
            }
        }
    }
}
