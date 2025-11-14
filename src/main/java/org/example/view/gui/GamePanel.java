package org.example.view.gui;

import org.example.model.jeu.Terrain;

import javax.swing.JPanel;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private final Terrain terrain;
    private final TileManager tileManager;

    public GamePanel(Terrain terrain) {
        this.terrain = terrain;
        this.tileManager = new TileManager(terrain);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < terrain.getHauteur(); y++) {
            for (int x = 0; x < terrain.getLargeur(); x++) {
                int tileId = terrain.getTileId(x, y);
                BufferedImage tile = tileManager.getTuile(tileId);

                if (tile != null) {
                    g.drawImage(tile, x * terrain.getTileWidth(),
                            y * terrain.getTileHeight(), this);
                }
            }
        }

    }


}