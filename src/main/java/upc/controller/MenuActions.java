package upc.controller;


import java.awt.event.ActionListener;
import java.io.IOException;

import upc.model.jeu.GameManager;
import upc.model.save.GameSave;
import upc.view.gui.GamePanel;
import upc.view.gui.SetupPanel;
import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;
import upc.view.gui.SettingsPanel;

public class MenuActions {

    /** Fenêtre principale de l'application */
    private final MainFrame mainFrame;

    /** Panneau du menu principal */
    private final MenuPanel menuPanel;


    /**
     * Construit un gestionnaire d'actions pour le menu.
     *
     * @param mainFrame La fenêtre principale.
     * @param menuPanel Le panneau du menu.
     */
    public MenuActions(MainFrame mainFrame, MenuPanel menuPanel) {
        this.mainFrame = mainFrame;
        this.menuPanel = menuPanel;
    }

    /**
     * Retourne l'action du bouton "Nouvelle Partie".
     * <p>
     * Affiche l'écran de configuration de partie.
     *
     * @return L'ActionListener pour démarrer une nouvelle partie.
     */
    public ActionListener getNewGameAction() {
        return e -> {
            SetupPanel setupPanel = new SetupPanel(mainFrame);

            mainFrame.setContentPane(setupPanel);
            mainFrame.revalidate();
            mainFrame.repaint();

            setupPanel.requestFocusInWindow();
        };
    }

    public ActionListener getLoadGameAction() {
        return e -> {
            try {
                GameSave save = GameSave.load();
                GameManager gameManager = save.toGameManager();
                GamePanel gamePanel = new GamePanel(gameManager);
                new GameController(gamePanel, gameManager);

                mainFrame.setContentPane(gamePanel);
                gamePanel.requestFocusInWindow();
                mainFrame.revalidate();
                mainFrame.repaint();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public ActionListener getSettingsAction() {
        return e -> {
            SettingsPanel settingsPanel = new SettingsPanel();
            new SettingsController(settingsPanel, mainFrame, menuPanel);

            mainFrame.setContentPane(settingsPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        };
    }

    public ActionListener getQuitAction() {
        return e -> System.exit(0);
    }
}
