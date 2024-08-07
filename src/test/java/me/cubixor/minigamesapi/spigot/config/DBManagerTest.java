package me.cubixor.minigamesapi.config;

import me.cubixor.minigamesapi.TestUtils;
import me.cubixor.minigamesapi.spigot.config.DBManager;
import me.cubixor.minigamesapi.spigot.config.stats.BasicStatsField;
import me.cubixor.minigamesapi.spigot.config.stats.StatsField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.stream.Collectors;

class DBManagerTest {

    DBManager dbManager;

    @BeforeEach
    void setUpDB() {
        dbManager = TestUtils.createDBManager();
    }

    @AfterEach
    void cleanup() {
        TestUtils.clearDB(dbManager);
    }

    @Test
    void connectionTest() {
        DataSource source = dbManager.getDataSource();
        try (Connection conn = source.getConnection()) {
            Assertions.assertTrue(conn.isValid(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createTableTest() {
        StatsField[] fields = BasicStatsField.values();
        dbManager.createStatsTable(Arrays.stream(fields).collect(Collectors.toList()));

        try (Connection conn = dbManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM stats")) {
            ResultSet result = statement.executeQuery();
            ResultSetMetaData resultMeta = result.getMetaData();

            Assertions.assertEquals(fields.length + 1, resultMeta.getColumnCount());

            for (int i = 0; i < fields.length; i++) {
                Assertions.assertEquals(fields[i].getCode(), resultMeta.getColumnLabel(i + 2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

}
