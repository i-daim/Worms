package org.example;

import org.example.controller.ControllerConsole;
import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Terrain;
import org.example.view.console.VueConsole;

public class MainConsole {
    public static void main(String[] args) {
        Equipe equipe = new Equipe("Team A", 1);
        Terrain terrain = Terrain.creerTerrainVide(100, 100);
        Worm worm = new Worm(100, equipe, 50, 50);
        VueConsole vue = new VueConsole();
        ControllerConsole controller = new ControllerConsole(terrain, worm, vue);
        controller.demarrer();
    }
}
