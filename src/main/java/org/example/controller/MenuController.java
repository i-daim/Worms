package org.example.controller;

import org.example.model.jeu.Partie;
import org.example.view.gui.GamePanel;
import org.example.view.gui.MainFrame;
import org.example.view.gui.MenuPanel;

public class MenuController {

    private final MainFrame mainFrame;
    private final MenuPanel menuPanel;

    public MenuController(MainFrame mainFrame, MenuPanel menuPanel) {
        this.mainFrame = mainFrame;
        this.menuPanel = menuPanel;
        addListeners();
    }

    public void addListeners() {
        menuPanel.addNewGameListener(e -> {
            Partie partie = new Partie("liberty.png");
            GamePanel gamePanel = new GamePanel(partie);

            new GameController(gamePanel, partie);

            mainFrame.setContentPane(gamePanel);
            mainFrame.revalidate();
            mainFrame.repaint();

            gamePanel.requestFocusInWindow();
        });

        menuPanel.addLoadGameListener(e -> {
            // à implémenter plus tard
        });

        menuPanel.addQuitterListener(e -> System.exit(0));
    }
}
