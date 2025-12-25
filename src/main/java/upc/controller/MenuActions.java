package upc.controller;

import upc.model.jeu.GameManager;
import upc.view.gui.GamePanel;
import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;
import upc.view.gui.SettingsPanel;

import java.awt.event.ActionListener;

public class MenuActions {

    private final MainFrame mainFrame;
    private final MenuPanel menuPanel;

    public MenuActions(MainFrame mainFrame, MenuPanel menuPanel) {
        this.mainFrame = mainFrame;
        this.menuPanel = menuPanel;
    }

    public ActionListener getNewGameAction() {
        return e -> {
            GameManager gameManager = new GameManager();
            GamePanel gamePanel = new GamePanel(gameManager);
            new GameController(gamePanel, gameManager);

            mainFrame.setContentPane(gamePanel);
            mainFrame.revalidate();
            mainFrame.repaint();

            gamePanel.requestFocusInWindow();
        };
    }

    public ActionListener getLoadGameAction() {
        return e -> System.out.println("Load Game non implémenté pour le moment.");
    }

    public ActionListener getSettingsAction() {
        return e -> {
            SettingsPanel settingsPanel = new SettingsPanel();

            settingsPanel.getBackButton().addActionListener(ev -> {
                mainFrame.setContentPane(menuPanel);
                mainFrame.revalidate();
                mainFrame.repaint();
            });

            mainFrame.setContentPane(settingsPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        };
    }

    public ActionListener getQuitAction() {
        return e -> System.exit(0);
    }
}
