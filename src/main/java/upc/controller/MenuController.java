package upc.controller;

import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;

public class MenuController {

    /** Panneau du menu principal contenant les boutons */
    private final MenuPanel menuPanel;

    /** Gestionnaire des actions associées aux boutons du menu */
    private final MenuActions menuActions;

    public MenuController(MainFrame mainFrame, MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
        this.menuActions = new MenuActions(mainFrame, menuPanel);
        addListeners();
    }

    private void addListeners() {
        menuPanel.addNewGameListener(menuActions.getNewGameAction());
        menuPanel.addLoadGameListener(menuActions.getLoadGameAction());
        menuPanel.addSettingsListener(menuActions.getSettingsAction());
        menuPanel.addQuitterListener(menuActions.getQuitAction());
    }
}
