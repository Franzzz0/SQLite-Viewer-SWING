package viewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
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
//            tablesComboBox.setEnabled(false);
            queryTextArea.setEnabled(false);
            executeButton.setEnabled(false);
            File file = new File(fileNameTextField.getText());
            if (!file.exists()) {
                JOptionPane.showMessageDialog(new JFrame(), "File doesn't exist!");
                return;
            }
            tablesComboBox.removeAllItems();
            queryTextArea.setText("");

            if (database.connect(fileNameTextField.getText())) {
                ArrayList<String> tables = database.getTables();
                for (String t : tables) tablesComboBox.addItem(t);
//                tablesComboBox.setEnabled(true);
                queryTextArea.setEnabled(true);
                executeButton.setEnabled(true);
            }
        });
        tablesComboBox.addActionListener(a -> {
            String selectedTable = (String)tablesComboBox.getSelectedItem();
            if (selectedTable != null) {
                queryTextArea.setText(DatabaseHandler.getSelectQuery(selectedTable));
            }
        });
        executeButton.addActionListener(a -> {
            String response = database.execute(queryTextArea.getText());
            if (!response.equals("OK")) {
                JOptionPane.showMessageDialog(new JFrame(), response);
            }
        });
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
