package org.example.controller;

import org.example.model.jeu.Terrain;
import org.example.model.jeu.TerrainGenerator;
import org.example.view.gui.GamePanel;
import org.example.view.gui.MenuPanel;

public class MenuController {

    private final MenuPanel menuPanel;

    public MenuController(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
        addListeners();
    }

    public void addListeners() {
        menuPanel.addNewGameListener(e -> {
            TerrainGenerator terrainGenerator = new TerrainGenerator();
            Terrain terrain = terrainGenerator.chargerTerrain("map0.json");
            GamePanel gamePanel = new GamePanel(terrain);
            gamePanel.setVisible(true);
            menuPanel.setVisible(false);
        });

        menuPanel.addLoadGameListener(e -> {
            // à implémenter plus tard
        });

        menuPanel.addQuitterListener(e -> System.exit(0));
    }
}
