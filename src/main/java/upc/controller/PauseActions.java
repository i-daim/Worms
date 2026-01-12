package upc.controller;

import upc.assets.SoundManager;
import upc.view.gui.GamePanel;
import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;
import upc.view.gui.SettingsPanel;

import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;
/**
 * Gestionnaire des actions du menu pause.
 * <p>
 * Cette classe crée et retourne les {@link ActionListener} pour chaque bouton
 * du menu pause (Reprendre, Sauvegarder, Menu principal, Paramètres, Quitter).
 */
public class PauseActions {

    /** Contrôleur principal du jeu pour accéder aux fonctions de sauvegarde et pause */
    private final GameController gameController;

    /** Panneau de jeu pour gérer l'affichage du menu pause */
    private final GamePanel gamePanel;

    /**
     * Construit un gestionnaire d'actions pour le menu pause.
     *
     * @param gameController Le contrôleur du jeu.
     * @param gamePanel      Le panneau de jeu.
     */
    public PauseActions(GameController gameController, GamePanel gamePanel) {
        this.gameController = gameController;
        this.gamePanel = gamePanel;
    }

    /**
     * Retourne l'action du bouton "Reprendre".
     * <p>
     * Désactive la pause, cache le menu pause et rend le focus au jeu.
     *
     * @return L'ActionListener pour reprendre la partie.
     */
    public ActionListener getResumeAction() {
        return e -> {
            GameController.togglePause();
            gamePanel.togglePauseMenu(false);
            gamePanel.requestFocusInWindow();
        };
    }

    /**
     * Retourne l'action du bouton "Sauvegarder".
     * <p>
     * Sauvegarde l'état actuel de la partie dans un fichier.
     *
     * @return L'ActionListener pour sauvegarder la partie.
     */
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

    /**
     * Retourne l'action du bouton "Menu Principal".
     * <p>
     * Arrête la partie en cours, lance la musique du menu et retourne au menu principal.
     *
     * @return L'ActionListener pour retourner au menu.
     */
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

    /**
     * Retourne l'action du bouton "Paramètres".
     * <p>
     * Affiche l'écran de paramètres depuis le menu pause. Le bouton retour
     * des paramètres ramène au jeu en pause.
     *
     * @return L'ActionListener pour accéder aux paramètres depuis la pause.
     */
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

    /**
     * Retourne l'action du bouton "Quitter".
     * <p>
     * Ferme l'application immédiatement sans sauvegarder.
     *
     * @return L'ActionListener pour quitter le jeu.
     */
    public ActionListener getQuitAction() {
        return e -> System.exit(0);
    }
}
