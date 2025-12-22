package upc.view.gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PausePanel extends JPanel {

    private int menuX = (int) (20 * GamePanel.ZOOM);
    private int menuY = (int) (15 * GamePanel.ZOOM);
    private int menuWidth = (int) (250 * GamePanel.ZOOM);
    private int menuHeight = (int) ((GamePanel.DEFAULT_HEIGHT - 300) * GamePanel.ZOOM);

    public PausePanel() {
        this.setBounds(0, 0, GamePanel.DEFAULT_WIDTH, GamePanel.DEFAULT_HEIGHT);
        this.setOpaque(false);
        this.setVisible(false);
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


}
