package me.cubixor.minigamesapi.spigot.config.stats;

public enum BasicStatsField implements StatsField {
    GAMES, WINS, LOOSES, PLAYTIME;


    @Override
    public String getCode() {
        return toString().toLowerCase();
    }
}
