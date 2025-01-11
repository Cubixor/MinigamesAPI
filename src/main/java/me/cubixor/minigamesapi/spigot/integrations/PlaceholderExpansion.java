package me.cubixor.minigamesapi.spigot.integrations;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.config.stats.BasicStatsField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsManager;
import me.cubixor.minigamesapi.spigot.game.ArenasRegistry;
import me.cubixor.minigamesapi.spigot.game.arena.Arena;
import me.cubixor.minigamesapi.spigot.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private final ArenasRegistry arenasRegistry;
    private final StatsManager statsManager;
    private final PlaceholderParser placeholderParser;

    public PlaceholderExpansion(ArenasRegistry arenasRegistry, StatsManager statsManager, PlaceholderParser placeholderParser) {
        this.arenasRegistry = arenasRegistry;
        this.statsManager = statsManager;
        this.placeholderParser = placeholderParser;
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
        String param2 = paramsSplit.length > 2 ? paramsSplit[2] : null;

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
            case "totalplayers": {
                return Integer.toString(arenasRegistry
                        .getAllArenas()
                        .mapToInt(arena -> arena.getPlayers().size())
                        .sum());
            }
            case "arena":
                if (player == null) return null;

                Arena arena = arenasRegistry.getPlayerArena(player.getName());
                if (arena == null) {
                    return "";
                }

                return arena.getName();
            case "playtime":
                if (player == null) return "0";

                return MessageUtils.convertPlaytime(statsManager.getCachedStats(player.getName(), BasicStatsField.PLAYTIME));
            case "top":
                if (param1 == null || param2 == null) return "-";

                int pos;
                try {
                    pos = Integer.parseInt(param2) - 1;
                } catch (NumberFormatException e) {
                    return "-";
                }

                if (pos < 0 || pos > 9) {
                    return "-";
                }

                if (param1.equals("player")) {
                    String target = statsManager.getPlayerFromCachedRanking(pos);
                    if (target == null) {
                        return "-";
                    }
                    return target;
                } else if (param1.equals("value")) {
                    return String.valueOf(statsManager.getValueFromCachedRanking(pos));
                }

                return "-";
            default:
                if (player == null) return "0";

                for (StatsField field : statsManager.getFields()) {
                    if (params.equals(field.getCode())) {
                        return String.valueOf(statsManager.getCachedStats(player.getName(), field));
                    }
                }
        }

        PlaceholderParseEvent placeholderParseEvent = new PlaceholderParseEvent(player, params);
        if (placeholderParser != null) {
            placeholderParser.onPlaceholderParse(placeholderParseEvent);
        }

        return placeholderParseEvent.getParsed();
    }
}
