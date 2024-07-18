package me.cubixor.minigamesapi.spigot.config.stats;

import me.cubixor.minigamesapi.TestUtils;
import me.cubixor.minigamesapi.spigot.config.DBManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

public class DBStatsManagerTest extends StatsManagerTest {

    private DBManager dbManager;
    private StatsManager statsManager;

    @BeforeEach
    void initStatsManager() {
        dbManager = TestUtils.createDBManager();
        statsManager = new DBStatsManager(Arrays.asList(BasicStatsField.values()), dbManager);
        dbManager.createStatsTable(Arrays.asList(BasicStatsField.values()));
    }

    @AfterEach
    void clearDB() {
        TestUtils.clearDB(dbManager);
    }

    @Override
    StatsManager createInstance() {
        return statsManager;
    }
}
