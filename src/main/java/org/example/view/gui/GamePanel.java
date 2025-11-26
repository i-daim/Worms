package org.example.view.gui;

import org.example.model.jeu.Terrain;

import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 720;

    private final Terrain terrain;
    private int cameraX = 0;
    private int cameraY = 0;

    public GamePanel(Terrain terrain) {
        this.terrain = terrain;
        this.setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g2.scale(getZoomX(), getZoomY());

        g2.translate(-cameraX, -cameraY);

        BufferedImage terrainImage = terrain.getImage();
        g2.drawImage(terrainImage, 0, 0, this);

        g2.translate(cameraX, cameraY);
        g2.scale(1.0 / getZoomX(), 1.0 / getZoomY());


    public Point screenToWorld(Point screenPoint) {
        double worldX = (screenPoint.x / getZoomX()) + cameraX;
        double worldY = (screenPoint.y / getZoomY()) + cameraY;
        return new Point((int) worldX, (int) worldY);
    }


    public double getZoomX() {
        return Math.max(1.0, (double) getWidth()/DEFAULT_WIDTH);
    }

    public double getZoomY() {
        return Math.max(1.0, (double) getHeight()/DEFAULT_HEIGHT);
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
