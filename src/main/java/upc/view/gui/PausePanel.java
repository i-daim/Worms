package upc.view.gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PausePanel extends JPanel {

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

        drawMenu(g2);
    }

    private void drawMenu(Graphics2D g2) {
        int width = (int) (250 * GamePanel.ZOOM);
        int height = (int) ((GamePanel.DEFAULT_HEIGHT - 300) * GamePanel.ZOOM);

        int x = (int) (20 * GamePanel.ZOOM);
        int y = (int) (15 * GamePanel.ZOOM);

        g2.setColor(new Color(250, 250, 250, 100));
        g2.fillRect(x, y, width + 10, height + 10);
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(x + 5, y + 5, width, height);
    }
}
