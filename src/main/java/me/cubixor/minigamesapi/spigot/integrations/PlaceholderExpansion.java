package me.cubixor.minigamesapi.spigot.integrations;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.stats.BasicStatsField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.game.ArenasRegistry;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private final ArenasRegistry arenasRegistry;
    private final StatsManager statsManager;

    public PlaceholderExpansion(ArenasRegistry arenasRegistry, StatsManager statsManager) {
        this.arenasRegistry = arenasRegistry;
        this.statsManager = statsManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return MinigamesAPI.getPlugin().getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return MinigamesAPI.getPlugin().getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return MinigamesAPI.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }


    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        String[] paramsSplit = params.split("_");

        if (paramsSplit.length < 1) {
            return null;
        }
        String param1 = paramsSplit.length > 1 ? paramsSplit[1] : null;

        switch (paramsSplit[0]) {
            case "status": {
                if (param1 == null) return null;

                Arena arena = arenasRegistry.getArena(param1);
                return MessageUtils.getStringState(arena);
            }
            case "players": {
                if (param1 == null) return null;

                Arena arena = arenasRegistry.getArena(param1);
                if (arena == null) {
                    return "0";
                }
                return Integer.toString(arena.getPlayers().size());
            }
            case "arena":
                if (player == null) return null;

                Arena arena = arenasRegistry.getPlayerArena(player.getName());
                if (arena == null) {
                    return "";
                }

                return arena.getName();
            case "playtime":
                if (player == null) return null;

                return MessageUtils.convertPlaytime(statsManager.getCachedStats(player.getName(), BasicStatsField.PLAYTIME));
            default:
                if (player == null) return null;

                for (StatsField field : statsManager.getFields()) {
                    if (params.equals(field.getCode())) {
                        return String.valueOf(statsManager.getCachedStats(player.getName(), field));
                    }
                }
        }

        PlaceholderParseEvent placeholderParseEvent = new PlaceholderParseEvent(player, params);
        Bukkit.getPluginManager().callEvent(placeholderParseEvent);

        return placeholderParseEvent.getParsed();
    }
}
