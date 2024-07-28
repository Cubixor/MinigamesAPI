package me.cubixor.minigamesapi.spigot.game.arena.phases;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.arena.GameState;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import me.cubixor.minigamesapi.spigot.utils.Sounds;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PhaseStarting extends GamePhase {

    private BukkitTask countdownTask;
    private BukkitTask xpTask;

    public PhaseStarting(LocalArena localArena) {
        super(localArena);
    }

    @Override
    public void run() {
        localArena.setTimer(MinigamesAPI.getPlugin().getConfig().getInt("waiting-time"));

        runXPCountdown();

        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                int time = localArena.getTimer();

                if (time <= 0) {
                    stop();
                    localArena.getStateManager().setGame();
                    return;
                }

                updateCosmetics(time);

                localArena.setTimer(localArena.getTimer() - 1);
            }

        }.runTaskTimer(MinigamesAPI.getPlugin(), 0, 20);
    }

    @Override
    public void stop() {
        countdownTask.cancel();
        xpTask.cancel();

        for (Player p : localArena.getBukkitPlayers()) {
            p.setLevel(0);
            p.setExp(0);
            //p.getInventory().clear();
            Titles.clearTitle(p);
            ActionBar.clearActionBar(p);
        }
    }

    @Override
    public GameState getGameState() {
        return GameState.STARTING;
    }

    private void updateCosmetics(int time) {
        for (Player p : localArena.getBukkitPlayers()) {
            p.setLevel(localArena.getTimer());
        }

        float pitch;
        int titleFade;
        int titleStay;

        if (time == 10 || time == 30 || time == 60) {
            pitch = 0.5f;
            titleFade = 10;
            titleStay = 50;
        } else if (time <= 5) {
            if (time == 1) {
                pitch = 2f;
            } else {
                pitch = 1f;
            }
            titleFade = 0;
            titleStay = 30;
        } else {
            return;
        }

        String title = Messages.get("game.countdown-starting-title", "%time%", String.valueOf(time));
        String subTitle = Messages.get("game.countdown-starting-subtitle", "%time%", String.valueOf(time));

        for (Player p : localArena.getBukkitPlayers()) {
            p.setLevel(localArena.getTimer());
            Sounds.playSoundWithPitch("countdown", p, pitch);
            Titles.sendTitle(p, titleFade, titleStay, titleFade, title, subTitle);
        }
    }

    private void runXPCountdown() {
        for (Player p : localArena.getBukkitPlayers()) {
            p.setLevel(localArena.getTimer());
            p.setExp(1F);
        }

        xpTask = new BukkitRunnable() {
            int counter = 0;
            private float exp;

            @Override
            public void run() {
                if (counter % 20 == 0) {
                    exp = 1f;
                } else {
                    exp -= 0.05f;
                }


                for (Player p : localArena.getBukkitPlayers()) {
                    p.setExp(exp);
                }

                counter++;

            }
        }.runTaskTimer(MinigamesAPI.getPlugin(), 0, 1);
    }
}
