package upc.view.gui;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PausePanel extends JPanel {

    private int menuX = (int) (20 * GamePanel.ZOOM);
    private int menuY = (int) (15 * GamePanel.ZOOM);
    private int menuWidth = (int) (250 * GamePanel.ZOOM);
    private int menuHeight = (int) ((GamePanel.DEFAULT_HEIGHT - 300) * GamePanel.ZOOM);

    private final JButton resumeButton;
    private final JButton settingsButton;
    private final JButton mainMenuButton;
    private final JButton quitButton;

    public PausePanel() {
        this.setLayout(null);
        this.setOpaque(false);
        this.setVisible(false);

        resumeButton = ButtonManager.createGameButton("Continue");
        settingsButton = ButtonManager.createGameButton("Settings");
        mainMenuButton = ButtonManager.createGameButton("Return to menu");
        quitButton = ButtonManager.createGameButton("Quit Game");

        initButtons();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(250, 250, 250, 100));
        g2.fillRect(menuX, menuY, menuWidth + 10, menuHeight + 10);
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(menuX + 5, menuY + 5, menuWidth, menuHeight);
    }

    private void initButtons() {
        int btnWidth = (int) (200 * GamePanel.ZOOM);
        int btnHeight = (int) (50 * GamePanel.ZOOM);

        // Pour centrer et espacer les boutons
        int btnX = menuX + (menuWidth - btnWidth) / 2 + 5;
        int btnY = menuY + 50;
        int gap = (int) (20 * GamePanel.ZOOM) + btnHeight;

        resumeButton.setBounds(btnX, btnY, btnWidth, btnHeight);
        settingsButton.setBounds(btnX, btnY + gap, btnWidth, btnHeight);
        mainMenuButton.setBounds(btnX, btnY + gap * 2, btnWidth, btnHeight);
        quitButton.setBounds(btnX, btnY + gap * 3, btnWidth, btnHeight);


        this.add(resumeButton);
        this.add(settingsButton);
        this.add(mainMenuButton);
        this.add(quitButton);
    }


    public JButton getResumeButton() {
        return resumeButton;
    }

    public JButton getMainMenuButton() {
        return mainMenuButton;
    }

    public JButton getSettingsButton() {
        return settingsButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

}
