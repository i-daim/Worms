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
    }
}
