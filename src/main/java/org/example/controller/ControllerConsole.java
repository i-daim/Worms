package org.example.controller;

import org.example.model.entites.Alien;
import org.example.model.jeu.GameManager;
import org.example.view.console.ConsoleView;

public class ControllerConsole {
    private final GameManager gameManager;
    private final ConsoleView vue;
    private boolean enCours;

    public ControllerConsole() {
        this.gameManager = new GameManager("");
        this.vue = new ConsoleView();
        this.enCours = true;
    }

    public void start() {
        vue.printWelcome();
        vue.printTerrain(gameManager);

        // Faire tomber les worms
        System.out.println("\n⏳ Les worms tombent...\n");

        for (int i = 0; i < 180; i++) {
            gameManager.update(1.0 / 60.0);
        }

        // Afficher l'état après la chute
        vue.printStateAfterFall(gameManager);

        while (enCours && !gameManager.isOver()) {
            vue.printGameState(gameManager);
            String command = vue.requestCommand();
            traiterCommande(command);
        }

        if (gameManager.isOver()) {
            vue.printGameOver(gameManager);
        }

        vue.close();
    }

    private void traiterCommande(String command) {
        switch (command) {
            case "gauche":
                boolean reussiteG = gameManager.getActiveWorm().moveLeft(gameManager.getTerrain());
                vue.printMovementResult(reussiteG, "gauche");
                if (reussiteG) {
                    gameManager.skipTurn();
                }
                break;

            case "droite":
                boolean reussiteD = gameManager.getActiveWorm().moveRight(gameManager.getTerrain());
                vue.printMovementResult(reussiteD, "droite");
                if (reussiteD) {
                    gameManager.skipTurn();
                }
                break;

            case "sauter":
                gameManager.getWorld().getPhysicsEngine().jumpWorm(gameManager.getActiveWorm());

                System.out.println("⏳ Le worm saute...");
                for (int i = 0; i < 120; i++) {
                    gameManager.update(1.0 / 60.0);
                }

                vue.printJumpResult(true);
                gameManager.skipTurn();
                break;

            case "attaquer":
                int damage = vue.requestInteger("💥 Combien de dégâts ? ");
                int x = vue.requestInteger("🎯 Coordonnée X de la cible : ");
                int y = vue.requestInteger("🎯 Coordonnée Y de la cible : ");

                Alien target = gameManager.attaquerPosition(x, y, damage);
                vue.printAttackResult(target, x, y, gameManager.getActiveTeam());

                if (target != null) {
                    gameManager.skipTurn();
                }
                break;

            case "passer":
                gameManager.skipTurn();
                vue.printTurnSkipped();
                break;

            case "aliens":
                vue.printAliensStats(gameManager);
                break;

            case "map":
                vue.printTerrain(gameManager);
                break;

            case "exit":
                enCours = false;
                vue.printGameAbandoned();
                break;

            default:
                vue.printUnknownCommand();
                break;
        }
    }
}
