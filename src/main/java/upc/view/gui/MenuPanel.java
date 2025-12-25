package upc.view.gui;

import upc.assets.ImageManager;
import upc.assets.SoundManager;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {

    private final Image background = ImageManager.get("menu_background");

    private final JButton newGame = ButtonManager.createGameButton("New Game");
    private final JButton loadGame = ButtonManager.createGameButton("Load Game");
    private final JButton settingsButton = ButtonManager.createGameButton("Settings");
    private final JButton quitGame = ButtonManager.createGameButton("Quit");


    public MenuPanel() {

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

        SoundManager.playMusic("Menu Theme");
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
        SoundManager.stopMusic();
    }
}
