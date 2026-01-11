package upc.view.gui;

import upc.controller.MenuController;
import javax.swing.JFrame;

/**
 * Fenêtre principale de l'application.
 * <p>
 * Cette classe hérite de {@link JFrame} et sert de conteneur racine pour l'ensemble du jeu.
 * Elle est responsable de :
 * <ul>
 * <li>La configuration initiale de la fenêtre (Titre, Taille, Fermeture).</li>
 * <li>L'initialisation de la vue de démarrage (Menu Principal).</li>
 * <li>La gestion du changement de vues (via {@link #setContentPane}).</li>
 * </ul>
 */
public class MainFrame extends JFrame {

    /**
     * Constructeur de la fenêtre principale.
     * <p>
     * Initialise les propriétés de la fenêtre :
     * <ul>
     * <li><b>Titre</b> : "Glorb26"</li>
     * <li><b>État</b> : Plein écran</li>
     * <li><b>Fermeture</b> : Arrêt du programme</li>
     * </ul>
     * Instancie également le {@link MenuPanel} et son contrôleur pour
     * démarrer l'application sur le menu.
     */
    public MainFrame() {
        this.setTitle("Glorb26");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        MenuPanel menuPanel = new MenuPanel();
        new MenuController(this, menuPanel);
        this.setContentPane(menuPanel);
        this.setVisible(true);
    }

}