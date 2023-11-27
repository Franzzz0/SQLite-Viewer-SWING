package viewer;

import org.sqlite.SQLiteDataSource;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler {
    private final SQLiteDataSource dataSource;
    private boolean connectionValid;
    private final DefaultTableModel tableModel;

    public DatabaseHandler(DefaultTableModel tableModel) {
        dataSource = new SQLiteDataSource();
        this.tableModel = tableModel;
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
                if (query.matches("SELECT.*")) {
                    ResultSet resultSet = statement.executeQuery(query);
                    updateTable(resultSet);
                } else {
                    boolean result = statement.execute(query);
                    System.out.println("query executed");
                    System.out.println("received data: " + result);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateTable(ResultSet resultSet) throws SQLException {
        tableModel.setRowCount(0);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        Object[] columns = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columns[i] = metaData.getColumnName(i + 1);
        }
        tableModel.setColumnIdentifiers(columns);
        while(resultSet.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row [i - 1] = resultSet.getObject(i);
            }
            tableModel.addRow(row);
        }
    }

    public boolean isConnectionValid() {
        return connectionValid;
    }
}