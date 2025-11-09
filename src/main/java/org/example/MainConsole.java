package org.example;

import org.example.controller.ControllerConsole;
import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Monde;
import org.example.view.console.VueConsole;

public final class MainConsole {

    public static void main(String[] args) {
        Equipe equipe = new Equipe("Team A", 1);
        //Monde monde = new Monde(10, 10);
        Worm worm = new Worm(100, equipe, 5, 5);

        VueConsole vue = new VueConsole();
        //ControllerConsole controller = new ControllerConsole(monde, worm, vue);
        //controller.demarrer();
    }
}