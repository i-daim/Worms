package upc.view.gui;

import upc.view.Sound;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.Font;

public class MenuPanel extends JPanel {
    private final JButton settingsButton = createGameButton("Settings");

    private final Sound menuMusic = new Sound("/sounds/menu.wav");

    private Image background;
    private final JButton newGame = createGameButton("New Game");
    private final JButton loadGame = createGameButton("Load Game");
    private final JButton quitGame = createGameButton("Quit");

    public MenuPanel() {
        // Chargement de l'image d'arrière plan
        try {
            background = new ImageIcon(
                    getClass().getResource("/menu/background.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de l'image");
        }

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(Box.createVerticalGlue());

        this.add(newGame);
        this.add(Box.createVerticalStrut(20)); // Ajout un espace entre les boutons
        this.add(loadGame);
        this.add(Box.createVerticalStrut(20)); // Ajout un espace entre les boutons
        this.add(settingsButton);
        this.add(Box.createVerticalStrut(20));

        this.add(Box.createVerticalGlue());

        menuMusic.setVolume(-10);
        menuMusic.loop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JButton createGameButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Choisit la couleur en fonction de si le bouton est survolé ou pas
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

        // Définit le style du bouton
        btn.setForeground(new Color(126, 217, 87));
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Définit les dimensions du bouton
        Dimension size = new Dimension(250, 60);
        btn.setMaximumSize(size); // Critical for BoxLayout
        btn.setPreferredSize(size);

        return btn;
    }

    public void addNewGameListener(ActionListener listener) {
        newGame.addActionListener(listener);
    }

    public void addLoadGameListener(ActionListener listener) {
        loadGame.addActionListener(listener);
    }

    public void addQuitterListener(ActionListener listener) {
        quitGame.addActionListener(listener);
    }

    public void addSettingsListener(ActionListener listener) {
        settingsButton.addActionListener(listener);
    }

    public void stopMusic() {
        menuMusic.stop();
    }
}
