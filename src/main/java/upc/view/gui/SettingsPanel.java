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
        backButton = createGameButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createVerticalGlue());
        this.add(title);
        this.add(Box.createVerticalStrut(30));
        this.add(backButton);
        this.add(Box.createVerticalGlue());
    }

    private JButton createGameButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(35, 45, 55));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(75, 85, 95));
                } else {
                    g2.setColor(new Color(55, 65, 75));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

                super.paintComponent(g);
            }
        };

        btn.setForeground(new Color(126, 217, 87));
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension size = new Dimension(250, 60);
        btn.setMaximumSize(size);
        btn.setPreferredSize(size);

        return btn;
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
