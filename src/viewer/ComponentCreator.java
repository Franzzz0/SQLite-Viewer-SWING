package viewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ComponentCreator {

    public JButton createOpenButton() {
        JButton openButton = new JButton("Open");
        openButton.setPreferredSize(new Dimension(80, 24));
        openButton.setName("OpenFileButton");
        return openButton;
    }

    public JTextField createFileNameTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(600, 25));
        textField.setName("FileNameTextField");
        return textField;
    }

    public JPanel createTopPanel(JComponent... components) {
        JPanel panel = new JPanel();
        for (JComponent component : components) {
            panel.add(component);
        }
        panel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
        return panel;
    }
}
