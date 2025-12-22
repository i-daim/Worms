package upc.controller;

import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.view.console.ConsoleView;

public class ControllerConsole {
    private final GameManager gameManager;
    private final ConsoleView view;
    private boolean enCours;

    public ControllerConsole() {
        this.gameManager = new GameManager();
        this.view = new ConsoleView();
        this.enCours = true;
    }

    public void start() {
        view.printWelcome();

        while (enCours && !gameManager.isOver()) {
            view.printGameState(gameManager);
            String command = view.requestCommand();
            requestCommand(command);
        }

        view.printGameOver(gameManager);
        view.close();
    }

    private void requestCommand(String command) {
        if (command == null) {
            return;
        }

        switch (command) {
            case "gauche":
                gameManager.getActiveAlien().moveLeft();
                // Ajout de gameManager en 3ème argument
                view.printMovementResult(true, "gauche", gameManager);
                skipTurn();
                break;

            case "droite":
                gameManager.getActiveAlien().moveRight();
                // Ajout de gameManager en 3ème argument
                view.printMovementResult(true, "droite", gameManager);
                skipTurn();
                break;

            case "sauter":
                gameManager.getActiveAlien().jump();
                // Ajout de gameManager en 2ème argument
                view.printJumpResult(true, gameManager);
                skipTurn();
                break;

            case "attaquer":
                int degats = view.requestInteger("Puissance du tir (ex: 10) : ");
                int x = view.requestInteger("Position X cible : ");
                int y = view.requestInteger("Position Y cible : ");

                Alien cible = gameManager.attackPosition(x, y, degats);
                view.printAttackResult(cible, x, y, gameManager.getActiveTeam());
                skipTurn();
                break;

            case "wait":
            case "passer":
                skipTurn();
                break;

            case "aliens":
                view.printAliensStats(gameManager);
                sleep(2000);
                break;

            case "exit":
                enCours = false;
                view.printGameAbandoned();
                break;

            default:
                view.printUnknownCommand();
                sleep(1000);
        }
    }

    private void skipTurn() {
        gameManager.skipTurn();
        view.printTurnSkipped();
        sleep(1000);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // Ignorer
        }
    }
}
