package upc.controller;

import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;

/**
 * Contrôleur du menu principal.
 * <p>
 * Cette classe relie les boutons du menu à leurs actions respectives
 * (Nouvelle partie, Charger, Paramètres, Quitter).
 */
public class MenuController {

    /** Panneau du menu principal contenant les boutons */
    private final MenuPanel menuPanel;

    /** Gestionnaire des actions associées aux boutons du menu */
    private final MenuActions menuActions;

    /**
     * Construit le contrôleur du menu et initialise les listeners.
     *
     * @param mainFrame La fenêtre principale de l'application.
     * @param menuPanel Le panneau du menu à contrôler.
     */
    public MenuController(MainFrame mainFrame, MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
        this.menuActions = new MenuActions(mainFrame, menuPanel);
        addListeners();
    }

    /**
     * Associe chaque bouton du menu à son action correspondante.
     * <p>
     * Cette méthode configure les listeners pour :
     * <ul>
     *     <li>Bouton Nouvelle Partie → écran de configuration</li>
     *     <li>Bouton Charger → charge une sauvegarde</li>
     *     <li>Bouton Paramètres → écran de paramètres</li>
     *     <li>Bouton Quitter → ferme l'application</li>
     * </ul>
     */
    private void addListeners() {
        menuPanel.addNewGameListener(menuActions.getNewGameAction());
        menuPanel.addLoadGameListener(menuActions.getLoadGameAction());
        menuPanel.addSettingsListener(menuActions.getSettingsAction());
        menuPanel.addQuitterListener(menuActions.getQuitAction());
    }
}
