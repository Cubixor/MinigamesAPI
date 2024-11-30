package me.cubixor.minigamesapi.spigot.integrations;

import org.bukkit.OfflinePlayer;

public class PlaceholderParseEvent {

    private final OfflinePlayer player;
    private final String params;
    private String parsed = null;

    public PlaceholderParseEvent(OfflinePlayer player, String params) {
        this.player = player;
        this.params = params;
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
