package me.cubixor.minigamesapi;


import me.cubixor.minigamesapi.spigot.config.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestUtils {

    private TestUtils() {
    }

    public static DBManager createDBManager() {
        return new DBManager(
                "jdbc:mysql://localhost:3306/minigame?useSSL=false&autoReConnect=true",
                "root",
                "",
                "stats");
    }

    public static void clearDB(DBManager dbManager) {
        try (Connection conn = dbManager.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement("DROP TABLE " + dbManager.getTable())) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbManager.closeConnection();
    }
}
