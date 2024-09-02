package me.cubixor.minigamesapi.spigot.game;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.events.GameChatEvent;
import me.cubixor.minigamesapi.spigot.game.arena.LocalArena;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import me.cubixor.minigamesapi.spigot.utils.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatBlocker implements Listener {

    private final ArenasRegistry arenasRegistry;
    private final boolean whitelist;
    private final List<String> commands;

    public ChatBlocker(ArenasRegistry arenasRegistry) {
        this.arenasRegistry = arenasRegistry;
        this.whitelist = MinigamesAPI.getPlugin().getConfig().getString("command-blocker").equals("WHITELIST");
        this.commands = MinigamesAPI.getPlugin().getConfig().getStringList("command-blocker-list");

        Bukkit.getServer().getPluginManager().registerEvents(this, MinigamesAPI.getPlugin());
    }

    @EventHandler
    public void blockCommand(PlayerCommandPreprocessEvent evt) {
        if (!arenasRegistry.isInArena(evt.getPlayer())) {
            return;
        }
        if (Permissions.has(evt.getPlayer(), "bypass")) {
            return;
        }

        JavaPlugin plugin = MinigamesAPI.getPlugin();
        PluginCommand pluginCmd = plugin.getCommand(MinigamesAPI.getPlugin().getName());

        String msgLowercase = evt.getMessage().toLowerCase();
        if (msgLowercase.startsWith("/" + pluginCmd.getName()) ||
                (!pluginCmd.getAliases().isEmpty() && msgLowercase.startsWith("/" + pluginCmd.getAliases().get(0)))) {
            return;
        }

        if ((whitelist && !isOnList(evt.getMessage()))
                || (!whitelist && isOnList(evt.getMessage()))) {
            evt.setCancelled(true);
            Messages.send(evt.getPlayer(), "game.command-blocked");
        }
    }

    private boolean isOnList(String message) {
        for (String s : commands) {
            if (message.toLowerCase().startsWith("/" + s)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent evt) {
        LocalArena localArena = arenasRegistry.getPlayerLocalArena(evt.getPlayer());

        if (localArena == null) {
            return;
        }
        if (!MinigamesAPI.getPlugin().getConfig().getBoolean("game-chat")) {
            return;
        }
        evt.setCancelled(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                Set<Player> receivers = new HashSet<>(localArena.getBukkitPlayers());
                GameChatEvent gameChatEvent = new GameChatEvent(localArena, evt.getPlayer(), receivers, evt.getMessage());
                Bukkit.getPluginManager().callEvent(gameChatEvent);

                if (!gameChatEvent.isCancelled()) {
                    gameChatEvent.getReceivers().forEach(p -> p.sendMessage(gameChatEvent.getMessage()));
                }

            }
        }.runTask(MinigamesAPI.getPlugin());
    }
}
