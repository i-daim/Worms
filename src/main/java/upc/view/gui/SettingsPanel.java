package upc.view.gui;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Font;

public class SettingsPanel extends JPanel {

    private final JButton backButton;
    private Image background;

    public SettingsPanel() {

        try {
            background = new ImageIcon(
                    getClass().getResource("/menu/background_settings.png")
            ).getImage();
        } catch (Exception e) {
            System.err.println("Erreur... impossible de charger l'image ");
        }

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton = MenuPanel.createGameButton("Back");

        this.add(Box.createVerticalGlue());
        this.add(title);
        this.add(Box.createVerticalStrut(30));
        this.add(backButton);
        this.add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}
