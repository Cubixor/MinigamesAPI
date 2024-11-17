package me.cubixor.minigamesapi.spigot.config;

import com.zaxxer.hikari.HikariDataSource;
import me.cubixor.minigamesapi.spigot.config.stats.StatsField;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private final HikariDataSource source;
    private final String table;

    //Sample address: jdbc:mysql://localhost:3306/minigame?useSSL=false&autoReConnect=true
    public DBManager(String address, String username, String password, String table) {
        HikariDataSource ds = new HikariDataSource();
        try {
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        } catch (RuntimeException e) {
            ds.setDriverClassName("com.mysql.jdbc.Driver");
        }
        ds.setJdbcUrl(address);
        ds.setUsername(username);
        ds.setPassword(password);

        this.source = ds;
        this.table = table;
    }

    //language=sql
    public void createStatsTable(List<StatsField> fields) {
        List<String> dbStatsLines = new ArrayList<>();
        for (StatsField field : fields) {
            dbStatsLines.add(field.getCode() + " INT NOT NULL DEFAULT 0, ");
        }

        String query = "CREATE TABLE IF NOT EXISTS " + table + " (player VARCHAR(16) NOT NULL, " +
                String.join(" ", dbStatsLines) +
                " PRIMARY KEY (player)) ";

        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void closeConnection() {
        source.close();
    }

    public DataSource getDataSource() {
        return source;
    }

    public String getTable() {
        return table;
    }
}
