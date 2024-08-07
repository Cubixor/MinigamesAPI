package me.cubixor.minigamesapi.spigot.game.arena.phases;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
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
        //localArena.setTimer(MinigamesAPI.getPlugin().getConfig().getInt("game-time"));
        start();

        gameTask = new BukkitRunnable() {
            @Override
            public void run() {
                int time = localArena.getTimer();

                localArena.getScoreboardManager().updateScoreboard();

                localArena.setTimer(time + 1);
            }

        }.runTaskTimer(MinigamesAPI.getPlugin(), 0, 20);
    }

    @Override
    public void stop() {
        gameTask.cancel();
    }

    private void start() {
        for(Player p : localArena.getBukkitPlayers()){
            p.getInventory().clear();
        }
    }

    public void finish(List<Player> winners){
        localArena.getStateManager().setEnd(winners);
    }


    @Override
    public GameState getGameState() {
        return GameState.GAME;
    }


}
