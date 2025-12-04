package upc.controller;

import upc.model.jeu.GameManager;
import upc.view.gui.GamePanel;
import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;

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
            GameManager gameManager = new GameManager("liberty");
            GamePanel gamePanel = new GamePanel(gameManager);

            new GameController(gamePanel, gameManager);

            menuPanel.stopMusic();

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
