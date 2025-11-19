package org.example.view.gui;

import org.example.model.jeu.Terrain;

import javax.swing.JPanel;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private final Terrain terrain;

    public GamePanel(Terrain terrain) {
        this.terrain = terrain;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        BufferedImage terrainImage = terrain.getImage();
        g.drawImage(terrainImage, 0, 0, this);

    }


}