package me.cubixor.minigamesapi.spigot.config.stats;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public abstract class StatsManager implements Listener {

    private final List<StatsField> fields;
    private final Map<String, Map<StatsField, Integer>> cachedStats = new HashMap<>();
    private LinkedHashMap<String, Integer> cachedRanking;

    protected StatsManager(List<StatsField> fields) {
        this.fields = fields;

        buildCache();

        Bukkit.getServer().getPluginManager().registerEvents(this, MinigamesAPI.getPlugin());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent evt) {
        new BukkitRunnable() {
            @Override
            public void run() {
                cacheStats(evt.getPlayer());
            }
        }.runTaskAsynchronously(MinigamesAPI.getPlugin());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        cachedStats.remove(evt.getPlayer().getName());
    }

    public int getCachedStats(String player, StatsField field) {
        if (cachedStats.containsKey(player)) {
            return cachedStats.get(player).get(field);
        } else {
            return 0;
        }
    }

    public int getStats(String player, StatsField field) {
        if (cachedStats.containsKey(player)) {
            return cachedStats.get(player).get(field);
        } else {
            return fetchSavedStats(player, field);
        }
    }

    public String getPlayerFromCachedRanking(int pos) {
        if (cachedRanking.size() <= pos) {
            return null;
        }

        return (String) cachedRanking.keySet().toArray()[pos];
    }

    public int getValueFromCachedRanking(int pos) {
        if (cachedRanking.size() <= pos) {
            return 0;
        }

        return (int) cachedRanking.values().toArray()[pos];
    }

    public void addStats(String player, StatsField field, int value) {
        if (cachedStats.containsKey(player)) {
            cachedStats.get(player).merge(field, value, Integer::sum);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                addSavedStats(player, field, value);
            }
        }.runTaskAsynchronously(MinigamesAPI.getPlugin());
    }

    public void addWin(String player) {
        addStats(player, BasicStatsField.WINS, 1);
        int wins = cachedStats.get(player).get(BasicStatsField.WINS);

        if (cachedRanking.containsKey(player)) {
            cachedRanking.replace(player, wins);
            Utils.sortByValueInPlace(cachedRanking);
        } else if (wins > getValueFromCachedRanking(10)) {
            cachedRanking.remove(getPlayerFromCachedRanking(10));
            cachedRanking.put(player, wins);
            Utils.sortByValueInPlace(cachedRanking);
        }
    }

    public void cacheStats(Player player) {
        Map<StatsField, Integer> stats = new HashMap<>();
        for (StatsField field : fields) {
            stats.put(field, fetchSavedStats(player.getName(), field));
        }
        cachedStats.put(player.getName(), stats);
    }

    public void buildCache() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(p -> cacheStats(p));
                cachedRanking = new LinkedHashMap<>(getRanking(BasicStatsField.WINS));
            }
        }.runTaskAsynchronously(MinigamesAPI.getPlugin());
    }

    protected abstract int fetchSavedStats(String player, StatsField field);

    protected abstract void addSavedStats(String player, StatsField field, int count);

    public abstract Set<String> getAllPlayers();

    public abstract Map<String, Integer> getRanking(StatsField field);

    public List<StatsField> getFields() {
        return fields;
    }
}
