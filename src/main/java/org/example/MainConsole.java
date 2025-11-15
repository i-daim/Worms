package org.example;

import org.example.controller.ControllerConsole;

public class MainConsole {
    public static void main(String[] args) {
        System.out.println("🎮 Lancement du jeu Worms...\n");

        ControllerConsole controller = new ControllerConsole("map0.json");
        controller.demarrer();
    }
}