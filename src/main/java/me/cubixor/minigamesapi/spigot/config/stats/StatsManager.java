package me.cubixor.minigamesapi.spigot.config.stats;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class StatsManager {

    private final List<StatsField> fields;

    protected StatsManager(List<StatsField> fields) {
        this.fields = fields;
    }

    public abstract int getStats(String player, StatsField field);

    public abstract void addStats(String player, StatsField field, int count);

    public abstract Set<String> getAllPlayers();

    public abstract Map<String, Integer> getRanking(StatsField field);

    public List<StatsField> getFields() {
        return fields;
    }
}
