package viewer;

import javax.swing.*;
import java.awt.*;

public class SQLiteViewer extends JFrame {

    public SQLiteViewer() {
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

        JLabel dataBaseLabel = new JLabel("Database:");
        JTextField fileNameTextField = creator.createFileNameTextField();
        JButton openButton = creator.createOpenButton();

        JPanel topPanel = creator.createTopPanel(
                dataBaseLabel,
                fileNameTextField,
                openButton);

        add(topPanel, BorderLayout.NORTH);
    }
}
