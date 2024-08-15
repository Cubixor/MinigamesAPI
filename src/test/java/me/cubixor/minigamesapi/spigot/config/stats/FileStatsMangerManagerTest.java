package me.cubixor.minigamesapi.spigot.config.stats;

import me.cubixor.minigamesapi.spigot.config.CustomConfig;

import java.util.Arrays;

public class FileStatsMangerManagerTest extends StatsManagerTest {

    @Override
    public StatsManager createInstance() {
        CustomConfig playersConfig = new CustomConfig("players.yml");
        return new FileStatsManager(Arrays.asList(BasicStatsField.values()), playersConfig);
    }
}
