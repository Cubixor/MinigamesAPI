package me.cubixor.minigamesapi.spigot.integrations;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlaceholderParseEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final OfflinePlayer player;
    private final String params;
    private String parsed = null;

    public PlaceholderParseEvent(OfflinePlayer player, String params) {
        this.player = player;
        this.params = params;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public String getParams() {
        return params;
    }

    public String getParsed() {
        return parsed;
    }

    public void setParsed(String parsed) {
        this.parsed = parsed;
    }
}
