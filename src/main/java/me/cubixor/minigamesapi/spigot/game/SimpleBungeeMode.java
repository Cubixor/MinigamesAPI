package me.cubixor.minigamesapi.spigot.game;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.events.GameLeaveEvent;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.utils.MessageUtils;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Optional;

public class SimpleBungeeMode implements Listener {

    private final ArenasManager arenasManager;

    public SimpleBungeeMode(ArenasManager arenasManager) {
        this.arenasManager = arenasManager;

        boolean simpleBungee = MinigamesAPI.getPlugin().getConfig().getBoolean("bungee.simple-bungee-mode");
        if (simpleBungee) {
            Bukkit.getServer().getPluginManager().registerEvents(this, MinigamesAPI.getPlugin());
        }
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent evt) {
        Optional<LocalArena> arenaOptional = arenasManager.getRegistry().getLocalArenas().values().stream().findFirst();
        if (!arenaOptional.isPresent()) return;
        LocalArena localArena = arenaOptional.get();

        boolean joined = arenasManager.getArenaPlayersManager().joinArena(evt.getPlayer(), localArena.getName());
        if (!joined) {
            evt.getPlayer().kickPlayer(Messages.get("bungee.kick-message"));
        }
    }

    @EventHandler
    public void onGameQuit(GameLeaveEvent evt) {
        evt.getPlayer().kickPlayer(Messages.get("bungee.leave-message"));
    }


    @EventHandler
    public void onPing(ServerListPingEvent evt) {
        Optional<LocalArena> arenaOptional = arenasManager.getRegistry().getLocalArenas().values().stream().findFirst();
        if (!arenaOptional.isPresent()) return;
        LocalArena arena = arenaOptional.get();

        String motd = Messages.get("bungee.motd", MessageUtils.getArenaStatusReplacement(arena));

        evt.setMotd(motd);
        evt.setMaxPlayers(arena.getMaxPlayers());
    }
}
