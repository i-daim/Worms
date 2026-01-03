package upc.controller;


import java.awt.event.ActionListener;
import upc.view.gui.GameSetupPanel;
import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;
import upc.view.gui.SettingsPanel;

public class MenuActions {

    private final MainFrame mainFrame;
    private final MenuPanel menuPanel;

    public MenuActions(MainFrame mainFrame, MenuPanel menuPanel) {
        this.mainFrame = mainFrame;
        this.menuPanel = menuPanel;
    }

    public ActionListener getNewGameAction() {
        return e -> {
            GameSetupPanel setupPanel = new GameSetupPanel(mainFrame);

            mainFrame.setContentPane(setupPanel);
            mainFrame.revalidate();
            mainFrame.repaint();

            setupPanel.requestFocusInWindow();
        };
    }

    public ActionListener getLoadGameAction() {
        return e -> System.out.println("Load Game non implémenté pour le moment.");
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
