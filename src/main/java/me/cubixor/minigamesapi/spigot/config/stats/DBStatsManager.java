package me.cubixor.minigamesapi.spigot.config.stats;


import me.cubixor.minigamesapi.spigot.config.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DBStatsManager extends StatsManager {

    private final DBManager dbManager;

    public DBStatsManager(List<StatsField> statsFields, DBManager dbManager) {
        super(statsFields);
        this.dbManager = dbManager;

        dbManager.createStatsTable(statsFields);
    }

    @Override
    public int getStats(String player, StatsField field) {
        //assert (!Bukkit.isPrimaryThread());

        String query = "SELECT $field FROM $table WHERE player=?"
                .replace("$table", dbManager.getTable())
                .replace("$field", field.getCode());

        try (Connection conn = dbManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)
        ) {
            statement.setString(1, player);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public void addStats(String player, StatsField field, int count) {
        //assert (!Bukkit.isPrimaryThread());

        String query = "INSERT INTO $table (player, $field) VALUES (?, ?) ON DUPLICATE KEY UPDATE $field = $field + VALUES($field);"
                .replace("$table", dbManager.getTable())
                .replace("$field", field.getCode());

        try (Connection conn = dbManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)
        ) {
            statement.setString(1, player);
            statement.setInt(2, count);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getAllPlayers() {
        //assert (!Bukkit.isPrimaryThread());

        String query = "SELECT player FROM $table"
                .replace("$table", dbManager.getTable());

        try (Connection conn = dbManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)
        ) {
            ResultSet result = statement.executeQuery();

            Set<String> players = new HashSet<>();
            while (result.next()) {
                players.add(result.getString("player"));
            }

            return players;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.emptySet();
    }

    @Override
    public Map<String, Integer> getRanking(StatsField field) {
        //assert (!Bukkit.isPrimaryThread());

        String query = "SELECT player, $field FROM $table ORDER BY $field DESC"
                .replace("$table", dbManager.getTable())
                .replace("$field", field.getCode());

        try (Connection connection = dbManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet results = statement.executeQuery();

            LinkedHashMap<String, Integer> ranking = new LinkedHashMap<>();
            while (results.next()) {
                ranking.put(results.getString("player"), results.getInt(field.getCode()));
            }
            return ranking;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.emptyMap();
    }
}
