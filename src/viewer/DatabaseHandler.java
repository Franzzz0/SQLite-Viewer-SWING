package viewer;

import org.sqlite.SQLiteDataSource;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler {
    private final SQLiteDataSource dataSource;
    private final DefaultTableModel tableModel;

    public DatabaseHandler(DefaultTableModel tableModel) {
        dataSource = new SQLiteDataSource();
        this.tableModel = tableModel;
    }

    public boolean connect(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5);
        } catch (SQLException e) {
            return false;
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

    public String execute(String query) {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                if (query.matches("SELECT.*")) {
                    ResultSet resultSet = statement.executeQuery(query);
                    updateTable(resultSet);
                } else {
                    statement.execute(query);
                }
                return "OK";
            }
        } catch (SQLException e) {
            return e.getMessage();
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
}