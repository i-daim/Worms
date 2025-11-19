package org.example.view.gui;

import org.example.model.jeu.Terrain;

import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private final Terrain terrain;
    private int cameraX = 0;

    public GamePanel(Terrain terrain) {
        this.terrain = terrain;
        this.setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        BufferedImage terrainImage = terrain.getImage();
        g.drawImage(terrainImage, 0, 0, this);

    }

    public void setCamera(int x) {
        cameraX = x;
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