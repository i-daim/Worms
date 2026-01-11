package upc.controller;

import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.jeu.Terrain;
import upc.model.jeu.TerrainGenerator;
import upc.view.console.ConsoleView;

public class ControllerConsole {

    /** Gestionnaire du jeu contenant la logique métier et l'état de la partie */
    private final GameManager gameManager;

    /** Vue console gérant l'affichage et la saisie utilisateur */
    private final ConsoleView view;

    /** Indicateur de continuation de la boucle de jeu */
    private boolean enCours;


    public ControllerConsole(GameManager gameManager) {
        Terrain terrainConsole = TerrainGenerator.createTerrain("liberty");
        this.gameManager = new GameManager(terrainConsole, 2);
        this.view = new ConsoleView();
        this.enCours = true;
    }


    public void start() {
        view.printWelcome();

        while (enCours && !gameManager.isOver()) {
            view.printGameState(gameManager);
            String command = view.requestCommand(gameManager);
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
                view.printMovementResult(true, "gauche", gameManager);
                skipTurn();
                break;

            case "droite":
                gameManager.getActiveAlien().moveRight();
                view.printMovementResult(true, "droite", gameManager);
                skipTurn();
                break;

            case "sauter":
                gameManager.getActiveAlien().jump();
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
        }
    }
}
