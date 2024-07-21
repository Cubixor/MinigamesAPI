package me.cubixor.minigamesapi.spigot.config.stats;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.Utils;
import me.cubixor.minigamesapi.spigot.config.CustomConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FileStatsManager extends StatsManager {

    private final CustomConfig statsConfig;
    private final ConfigurationSection playersSection;

    public FileStatsManager(List<StatsField> fields, CustomConfig statsConfig) {
        super(fields);
        this.statsConfig = statsConfig;
        this.playersSection = statsConfig.get().getConfigurationSection("players");

        startFileSaver();
    }

    @Override
    public int getStats(String player, StatsField field) {
        if (playersSection.getConfigurationSection(player) == null) {
            return 0;
        }

        return playersSection
                .getConfigurationSection(player)
                .getInt(field.getCode());
    }

    @Override
    public void addStats(String player, StatsField field, int count) {
        ConfigurationSection playerSection = playersSection.getConfigurationSection(player);
        if (playerSection == null) {
            playerSection = playersSection.createSection(player);
        }
        int updated = playerSection.getInt(field.getCode()) + count;
        playerSection.set(field.getCode(), updated);
    }

    @Override
    public Set<String> getAllPlayers() {
        if (playersSection != null) {
            return playersSection.getKeys(false);
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public Map<String, Integer> getRanking(StatsField field) {
        Set<String> players = getAllPlayers();
        LinkedHashMap<String, Integer> ranking = new LinkedHashMap<>();

        players.forEach(p -> ranking.put(p, getStats(p, field)));
        Utils.sortByValueInPlace(ranking);

        return ranking;
    }


    public void startFileSaver() {
        new BukkitRunnable() {
            @Override
            public void run() {
                statsConfig.save();
            }
        }.runTaskTimerAsynchronously(MinigamesAPI.getPlugin(), 600, 600);
    }
}
