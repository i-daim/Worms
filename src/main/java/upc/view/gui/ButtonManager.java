package upc.view.gui;

import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

public class ButtonManager {

    public static JButton createGameButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Change la couleur quand le bouton est survolé/appuyé
                if (getModel().isPressed()) {
                    g2.setColor(new Color(35, 45, 55));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(75, 85, 95));
                } else {
                    g2.setColor(new Color(55, 65, 75));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Bordure du bouton
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

                super.paintComponent(g);
            }
        };

        // Style du texte affiché
        btn.setForeground(new Color(126, 217, 87));
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Dimensions du bouton
        Dimension size = new Dimension(250, 60);
        btn.setMaximumSize(size); // pour avoir les mm dimensions
        btn.setPreferredSize(size);

        return btn;
    }

    public static JButton createGameButton(String text, ActionListener listener) {
        JButton btn = createGameButton(text);
        btn.addActionListener(listener);
        return btn;
    }
}
