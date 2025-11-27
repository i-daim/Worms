package org.example.view.gui;

import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Partie;
import org.example.model.jeu.Terrain;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 720;

    private final Terrain terrain;
    private final Partie partie;
    private int cameraX = 0;
    private int cameraY = 0;

    public GamePanel(Terrain terrain, Partie partie) {
        this.terrain = terrain;
        this.partie = partie;
        this.setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.scale(getZoomX(), getZoomY());
        g2.translate(-cameraX, -cameraY);

        BufferedImage terrainImage = terrain.getImage();
        g2.drawImage(terrainImage, 0, 0, this);

        drawWorms(g2);

        g2.translate(cameraX, cameraY);
        g2.scale(1.0 / getZoomX(), 1.0 / getZoomY());
    }

    public double getZoomX() {
        return Math.max(1.0, (double) this.getWidth() / DEFAULT_WIDTH);
    }

    public double getZoomY() {
        return Math.max(1.0, (double) this.getHeight() / DEFAULT_HEIGHT);
    }

    public void setCamera(int x, int y) {
        cameraX = x;
        cameraY = y;
        repaint();
    }

    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public Terrain getTerrain() {
        return terrain;
    }
}
