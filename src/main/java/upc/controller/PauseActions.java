package upc.controller;

import upc.assets.SoundManager;
import upc.view.gui.GamePanel;
import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;
import upc.view.gui.SettingsPanel;

import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;

public class PauseActions {

    /** Contrôleur principal du jeu pour accéder aux fonctions de sauvegarde et pause */
    private final GameController gameController;

    /** Panneau de jeu pour gérer l'affichage du menu pause */
    private final GamePanel gamePanel;

    public PauseActions(GameController gameController, GamePanel gamePanel) {
        this.gameController = gameController;
        this.gamePanel = gamePanel;
    }

    public ActionListener getResumeAction() {
        return e -> {
            GameController.togglePause();
            gamePanel.togglePauseMenu(false);
            gamePanel.requestFocusInWindow();
        };
    }

    public ActionListener getSaveGameAction() {
        return e -> {
            try {
                gameController.saveGame();
            } catch (Exception ex) {
                System.err.println("Erreur lors de la sauvegarde :"
                        + ex.getMessage());
            }
        };
    }

    public ActionListener getMainMenuAction() {
        return e -> {
            MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(gamePanel);
            if (mainFrame != null) {
                MenuPanel menuPanel = new MenuPanel();
                new MenuController(mainFrame, menuPanel);

                SoundManager.playMusic("Menu Theme");

                mainFrame.setContentPane(menuPanel);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        };
    }

    public ActionListener getSettingsAction() {
        return e -> {

             MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(gamePanel);
             if (mainFrame != null) {
                 SettingsPanel settingsPanel = new SettingsPanel();
                 new SettingsController(settingsPanel, mainFrame, null);

                 settingsPanel.getBackButton().addActionListener(e2 -> {
                     mainFrame.setContentPane(gamePanel);
                     mainFrame.revalidate();
                     mainFrame.repaint();
                     gamePanel.requestFocusInWindow();
                 });

                 mainFrame.setContentPane(settingsPanel);
                 mainFrame.revalidate();
                 mainFrame.repaint();
             }
        };
    }

    public ActionListener getQuitAction() {
        return e -> System.exit(0);
    }
}
