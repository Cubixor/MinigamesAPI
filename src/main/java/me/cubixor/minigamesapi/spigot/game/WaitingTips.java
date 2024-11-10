package me.cubixor.minigamesapi.spigot.game;

import com.cryptomorin.xseries.messages.ActionBar;
import com.google.common.collect.ImmutableMap;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;


public class WaitingTips {

    private final Random random = new Random();

    public WaitingTips(ArenasRegistry arenasRegistry) {
        runTipTask(arenasRegistry);
    }

    public void runTipTask(ArenasRegistry arenasRegistry) {
        List<String> tips = Messages.getList("other.tips",
                ImmutableMap.of("%tip-prefix%", Messages.get("other.tip-prefix")));

        new BukkitRunnable() {
            @Override
            public void run() {
                String tip = tips.get(random.nextInt(tips.size()));

                for (LocalArena localArena : arenasRegistry.getLocalArenas().values()) {
                    if (!localArena.getState().isWaitingStarting()) {
                        continue;
                    }

                    for (Player player : localArena.getBukkitPlayers()) {
                        ActionBar.sendActionBar(MinigamesAPI.getPlugin(), player, tip, 100);
                    }
                }
            }
        }.runTaskTimer(MinigamesAPI.getPlugin(), 0, Math.round(MinigamesAPI.getPlugin().getConfig().getDouble("tip-rate") * 20));
    }

}
