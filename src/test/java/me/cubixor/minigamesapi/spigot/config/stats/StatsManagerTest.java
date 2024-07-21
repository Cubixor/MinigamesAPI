package me.cubixor.minigamesapi.spigot.config.stats;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.cubixor.minigamesapi.spigot.MinigamesAPI;
import me.cubixor.minigamesapi.spigot.MockMain;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class StatsManagerTest {

    protected ServerMock mock;
    protected JavaPlugin plugin;

    abstract StatsManager createInstance();

    @BeforeEach
    public void setUp() {
        mock = MockBukkit.mock();
        plugin = MockBukkit.load(MockMain.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testStatsSaving() {
        StatsManager statsManager = createInstance();

        statsManager.addStats("Player1", BasicStatsField.WINS, 1);
        statsManager.addStats("Player1", BasicStatsField.LOOSES, 2);
        int wins = statsManager.getStats("Player1", BasicStatsField.WINS);
        int looses = statsManager.getStats("Player1", BasicStatsField.LOOSES);

        Assertions.assertEquals(1, wins);
        Assertions.assertEquals(2, looses);
    }


    @Test
    void testRanking() {
        StatsManager statsManager = createInstance();

        statsManager.addStats("Player2", BasicStatsField.GAMES, 2);
        statsManager.addStats("Player1", BasicStatsField.GAMES, 1);
        statsManager.addStats("Player3", BasicStatsField.GAMES, 3);

        Map<String, Integer> ranking = statsManager.getRanking(BasicStatsField.GAMES);
        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("Player3", 3);
        expected.put("Player2", 2);
        expected.put("Player1", 1);

        Assertions.assertEquals(expected, ranking);
    }


    @Test
    void testPlayers() {
        StatsManager statsManager = createInstance();

        statsManager.addStats("Player1", BasicStatsField.GAMES, 1);
        statsManager.addStats("Player2", BasicStatsField.WINS, 2);
        statsManager.addStats("Player1", BasicStatsField.LOOSES, 3);

        Set<String> players = statsManager.getAllPlayers();
        Set<String> expected = new LinkedHashSet<>();
        expected.add("Player1");
        expected.add("Player2");

        Assertions.assertEquals(expected, players);
    }

}
