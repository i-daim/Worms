package upc;

import upc.controller.ControllerConsole;

public class MainConsole {
    public static void main(String[] args) {
        System.out.println("🎮 Lancement du jeu Glorb26...\n");

        ControllerConsole controller = new ControllerConsole();
        controller.start();
    }
}