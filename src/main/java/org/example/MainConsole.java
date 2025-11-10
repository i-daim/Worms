package org.example;

import org.example.controller.ControllerConsole;
import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Terrain;
import org.example.view.console.VueConsole;

public final class MainConsole {

    public static void main(String[] args) {
        // Créer une équipe
        Equipe equipe = new Equipe("Team A", 1);

        // Créer un terrain vide de 100x100
        Terrain terrain = Terrain.creerTerrainVide(100, 100);

        // Créer un worm avec 100 points de vie à la position (5, 5)
        Worm worm = new Worm(100, equipe, 5, 5);

        // Créer la vue console
        VueConsole vue = new VueConsole();

        // Créer le contrôleur
        ControllerConsole controller = new ControllerConsole(terrain, worm, vue);

        // Démarrer l'application
        controller.demarrer();
    }
}
