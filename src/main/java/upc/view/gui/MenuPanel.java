package upc.view.gui;

import upc.assets.Sound;
import upc.assets.SoundManager;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private final JButton settingsButton = createGameButton("Settings");

    private final Sound menuMusic = SoundManager.getMusic("Menu Theme");
    private final JButton newGame = ButtonManager.createGameButton("New Game");
    private final JButton loadGame = ButtonManager.createGameButton("Load Game");
    private final JButton quitGame = ButtonManager.createGameButton("Quit");

    private Image background;

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
        this.add(Box.createVerticalStrut(20));
        this.add(loadGame);
        this.add(Box.createVerticalStrut(20));
        this.add(settingsButton);
        this.add(Box.createVerticalStrut(20));
        this.add(quitGame);
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
