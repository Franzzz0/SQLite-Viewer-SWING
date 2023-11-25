package viewer;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseHandler {
    private final SQLiteDataSource dataSource;
    private boolean connectionValid;

    public DatabaseHandler() {
        dataSource = new SQLiteDataSource();
        connectionValid = false;
    }

    public void connect(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                connectionValid = true;
            }
        } catch (SQLException e) {
            connectionValid = false;
            System.out.println("Connection error");
            System.out.println(e.getMessage());
        }
    }
    public static String getSelectQuery(String selectedTable) {
        return "SELECT * FROM " + selectedTable.toUpperCase() + ";";
    }

    public ArrayList<String> getTables() {
        ArrayList<String> tables = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                String query = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        tables.add(resultSet.getString("name"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection error");
            System.out.println(e.getMessage());
        }
        return tables;
    }

    public void execute(String query) {
        if (!connectionValid) {
            return;
        }
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                boolean result = statement.execute(query);
                System.out.println("query executed");
                System.out.println("received data: " + result);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isConnectionValid() {
        return connectionValid;
    }
}