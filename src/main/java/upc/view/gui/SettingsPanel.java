package upc.view.gui;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

public class SettingsPanel extends JPanel {

    private final JButton backButton = new JButton("Back");

    public SettingsPanel() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(200, 50));

        this.add(Box.createVerticalGlue());
        this.add(title);
        this.add(Box.createVerticalStrut(30));
        this.add(backButton);
        this.add(Box.createVerticalGlue());
    }

    public JButton getBackButton() {
        return backButton;
    }
}
