package viewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import java.awt.*;

public class ComponentCreator {

    private final Dimension buttonSize = new Dimension(80, 24);
    private final Dimension labelSize = new Dimension(60,25);


    public JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(labelSize);
        return label;
    }

    public JButton createOpenButton() {
        return createButton("Open", "OpenFileButton");
    }

    public JButton createExecuteButton() {
        JButton button = createButton("Execute", "ExecuteQueryButton");
        button.setEnabled(false);
        return button;
    }

    private JButton createButton(String text, String name) {
        JButton openButton = new JButton(text);
        openButton.setPreferredSize(buttonSize);
        openButton.setName(name);
        return openButton;
    }

    public JTextField createFileNameTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(600, 25));
        textField.setName("FileNameTextField");
        return textField;
    }

    public JComboBox<String> createTablesComboBox() {
        JComboBox<String> tablesComboBox = new JComboBox<>();
        tablesComboBox.setName("TablesComboBox");
        tablesComboBox.setPreferredSize(new Dimension(685, 25));
        tablesComboBox.setEnabled(false);
        return tablesComboBox;
    }


    public JTextArea createQueryTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setName("QueryTextArea");
        textArea.setEnabled(false);
        return textArea;
    }

    public JScrollPane createScrollPane(JTextArea textArea) {
        JScrollPane pane = new JScrollPane(textArea);
        pane.setPreferredSize(new Dimension(600, 60));
        return pane;
    }

    public JPanel createPanel(JComponent... components) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(790, 140));
        for (JComponent component : components) {
            panel.add(component);
        }
        panel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
        return panel;
    }

    public JTable getTable(TableModel model) {
        JTable table = new JTable(model);
        table.setName("Table");
        table.setEnabled(false);
        return table;
    }
}