package upc;

import upc.controller.ControllerConsole;
import upc.model.jeu.GameManager;

public class MainConsole {
    public static void main(String[] args) {
        System.out.println("🎮 Lancement du jeu Glorb26...\n");

        GameManager game = new GameManager("");
        ControllerConsole controller = new ControllerConsole(game);

        controller.start();
    }
}
