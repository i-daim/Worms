package upc.controller;

import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;

public class MenuController {

    private final MenuPanel menuPanel;
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
