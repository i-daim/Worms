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

}