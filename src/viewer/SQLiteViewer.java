package viewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SQLiteViewer extends JFrame {

    private final DatabaseHandler database;
    private final DefaultTableModel tableModel;

    public SQLiteViewer() {
        tableModel = new DefaultTableModel();
        database = new DatabaseHandler(tableModel);
        this.setTitle("SQLite Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        initializeComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        ComponentCreator creator = new ComponentCreator();

        JLabel dataBaseLabel = creator.createLabel("Database:");
        JTextField fileNameTextField = creator.createFileNameTextField();
        JButton openButton = creator.createOpenButton();
        JLabel tableLabel = creator.createLabel("Table:");
        JComboBox<String> tablesComboBox = creator.createTablesComboBox();
        JLabel queryLabel = creator.createLabel("Query:");
        JTextArea queryTextArea = creator.createQueryTextArea();
        JScrollPane queryScrollPane = creator.createScrollPane(queryTextArea);
        JButton executeButton = creator.createExecuteButton();

        JTable table = creator.getTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(new EmptyBorder(new Insets(0, 5, 5, 5)));

        openButton.addActionListener(a -> {
            tablesComboBox.removeAllItems();
            queryTextArea.setText("");
            database.connect(fileNameTextField.getText());
            if (database.isConnectionValid()) {
                ArrayList<String> tables = database.getTables();
                for (String t : tables) tablesComboBox.addItem(t);
            }
        });
        tablesComboBox.addActionListener(a -> {
            String selectedTable = (String)tablesComboBox.getSelectedItem();
            if (selectedTable != null) {
                queryTextArea.setText(DatabaseHandler.getSelectQuery(selectedTable));
            }
        });
        executeButton.addActionListener(a -> database.execute(queryTextArea.getText()));
        JPanel panel = creator.createPanel(
                dataBaseLabel,
                fileNameTextField,
                openButton,
                tableLabel,
                tablesComboBox,
                queryLabel,
                queryScrollPane,
                executeButton
        );

        add(panel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }
}
