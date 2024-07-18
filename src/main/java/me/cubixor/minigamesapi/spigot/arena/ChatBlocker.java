package me.cubixor.minigamesapi.spigot.arena;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.utils.Messages;
import me.cubixor.minigamesapi.spigot.utils.Permissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class ChatBlocker implements Listener {

    private final ArenasManager arenasManager;
    private final boolean whitelist;
    private final List<String> commands;

    public ChatBlocker(ArenasManager arenasManager) {
        this.arenasManager = arenasManager;
        this.whitelist = MinigamesAPI.getInstance().getConfig().getString("command-blocker").equals("WHITELIST");
        this.commands = MinigamesAPI.getInstance().getConfig().getStringList("command-blocker-list");
    }

    @EventHandler
    public void blockCommand(PlayerCommandPreprocessEvent evt) {
        if (!arenasManager.isInArena(evt.getPlayer())) {
            return;
        }
        if (Permissions.has(evt.getPlayer(), "bypass")) {
            return;
        }
        //TODO Proper command
        if (evt.getMessage().startsWith("/minewars") || evt.getMessage().startsWith("/mw")) {
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
}
