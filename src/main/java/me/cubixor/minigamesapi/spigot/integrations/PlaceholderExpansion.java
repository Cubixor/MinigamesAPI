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

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.register();
        }
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

        if (paramsSplit.length < 1 || (paramsSplit.length == 1 && player == null)) {
            return null;
        }
        String param1 = paramsSplit.length > 1 ? paramsSplit[1] : player.getName();

        switch (paramsSplit[0]) {
            case "status": {
                Arena arena = arenasRegistry.getArena(param1);
                return MessageUtils.getStringState(arena);
            }
            case "players": {
                Arena arena = arenasRegistry.getArena(param1);
                if (arena == null) {
                    return "0";
                }
                return Integer.toString(arena.getPlayers().size());
            }
            case "playtime":
                return MessageUtils.convertPlaytime(statsManager.getCachedStats(player.getName(), BasicStatsField.PLAYTIME));
            default:
                for (StatsField field : statsManager.getFields()) {
                    if (paramsSplit[0].equals(field.getCode())) {
                        return String.valueOf(statsManager.getCachedStats(player.getName(), field));
                    }
                }
        }

        return null;
    }
}
