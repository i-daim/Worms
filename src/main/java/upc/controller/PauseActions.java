package upc.controller;

import upc.view.gui.GamePanel;
import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;
import upc.view.gui.SettingsPanel;

import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;

public class PauseActions {

    private final GamePanel gamePanel;

    public PauseActions(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public ActionListener getResumeAction() {
        return e -> {
            gamePanel.togglePauseMenu(false);
            gamePanel.requestFocusInWindow();
        };
    }

    public ActionListener getNewGameAction() {
        return e -> {
            MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(gamePanel);
            if (mainFrame != null) {
                MenuPanel menuPanel = new MenuPanel();

                gamePanel.stopMusic();

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

                 settingsPanel.getBackButton().addActionListener(ev -> {
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
