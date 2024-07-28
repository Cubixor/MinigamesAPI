package me.cubixor.minigamesapi.spigot.config.stats;

import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class StatsManager implements Listener {

    private final List<StatsField> fields;
    private final Map<String, Map<StatsField, Integer>> cachedStats = new HashMap<>();

    protected StatsManager(List<StatsField> fields) {
        this.fields = fields;

        Bukkit.getServer().getPluginManager().registerEvents(this, MinigamesAPI.getPlugin());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent evt) {
        Map<StatsField, Integer> stats = new HashMap<>();
        for (StatsField field : fields) {
            stats.put(field, fetchSavedStats(evt.getPlayer().getName(), field));
        }
        cachedStats.put(evt.getPlayer().getName(), stats);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        cachedStats.remove(evt.getPlayer().getName());
    }

    public int getCachedStats(String player, StatsField field) {
        if (cachedStats.containsKey(player)) {
            return cachedStats.get(player).get(field);
        } else {
            return -1;
        }
    }

    public int getStats(String player, StatsField field) {
        if (cachedStats.containsKey(player)) {
            return cachedStats.get(player).get(field);
        } else {
            return fetchSavedStats(player, field);
        }
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

    public abstract int fetchSavedStats(String player, StatsField field);

    public abstract void addSavedStats(String player, StatsField field, int count);

    public abstract Set<String> getAllPlayers();

    public abstract Map<String, Integer> getRanking(StatsField field);

    public List<StatsField> getFields() {
        return fields;
    }
}
