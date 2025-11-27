package org.example.view.gui;

import org.example.model.entites.Worm;
import org.example.model.jeu.Partie;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class HUDPanel extends JPanel {

    private final Partie partie;

    public HUDPanel(Partie partie) {
        this.partie = partie;

        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(800, 100));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawTopBar(g2);
    }

    private void drawTopBar(Graphics2D g2) {
        int width = this.getWidth();
        int height = 50;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, width, height);
    }

    private void drawWormHealthBar(Graphics2D g2, Worm worm, boolean active) {
        int posX = worm.getPosition().getX();
        int posY = worm.getPosition().getY();

        int barWidth = 30;
        int barHeight = 5;
        int barX = posX - barWidth / 2;
        int barY = posY - 15;

        g2.setColor(Color.BLACK);
        g2.fillRect(barX-1, barY-1, barWidth+2, barHeight+2);

        // Barre de vie
        int hp = worm.getHealthPoints();
        int healthWidth = (int) ((hp / 100.0) * barWidth);

        Color healthColor = hp > 60 ? Color.GREEN : hp > 25 ? Color.ORANGE : Color.RED;
        g2.setColor(healthColor);
        g2.fillRect(barX, barY, healthWidth, barHeight);

        // Inidicateur en triangle si le worm est actif
        if (active) {
            g2.setColor(Color.YELLOW);
            g2.drawRect(barX-2, barY-2, barWidth+4, barHeight+4);

            int[] xPoints = {posX, posX - 4, posX + 4};
            int[] yPoints = {barY - 6, barY - 2, barY - 2};
            g2.fillPolygon(xPoints, yPoints, 3);
        }


    }



}
