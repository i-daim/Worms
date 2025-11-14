package org.example;

import org.example.controller.ControllerConsole;
import org.example.model.jeu.Terrain;
import org.example.model.jeu.TerrainGenerator;

public class MainConsole {
    public static void main(String[] args) {
        System.out.println("🎮 Lancement du jeu Worms...\n");

        TerrainGenerator terrainGenerator = new TerrainGenerator();
        Terrain terrain = terrainGenerator.chargerTerrain("tempMap.json");

        ControllerConsole controller = new ControllerConsole(terrain);
        controller.demarrer();
    }
}