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

    public void demarrer() {
        vue.afficherBienvenue();
        vue.afficherTerrain(gameManager);

        // Faire tomber les worms
        System.out.println("\n⏳ Les worms tombent...\n");

        for (int i = 0; i < 180; i++) {
            gameManager.update(1.0 / 60.0);
        }

        // Afficher l'état après la chute
        vue.afficherEtatApresChute(gameManager);

        while (enCours && !gameManager.isOver()) {
            vue.afficherEtatPartie(gameManager);
            String commande = vue.demanderCommande();
            traiterCommande(commande);
        }

        if (gameManager.isOver()) {
            vue.afficherFinPartie(gameManager);
        }

        vue.close();
    }

    private void traiterCommande(String commande) {
        switch (commande) {
            case "gauche":
                boolean reussiteG = gameManager.getActiveWorm().moveLeft(gameManager.getTerrain());
                vue.afficherResultatDeplacement(reussiteG, "gauche");
                if (reussiteG) {
                    gameManager.skipTurn();
                }
                break;

            case "droite":
                boolean reussiteD = gameManager.getActiveWorm().moveRight(gameManager.getTerrain());
                vue.afficherResultatDeplacement(reussiteD, "droite");
                if (reussiteD) {
                    gameManager.skipTurn();
                }
                break;

            case "sauter":
                gameManager.getMonde().getPhysicsEngine().jumpWorm(gameManager.getActiveWorm());

                System.out.println("⏳ Le worm saute...");
                for (int i = 0; i < 120; i++) {
                    gameManager.update(1.0 / 60.0);
                }

                vue.afficherResultatSaut(true);
                gameManager.skipTurn();
                break;

            case "attaquer":
                int degats = vue.demanderEntier("💥 Combien de dégâts ? ");
                int x = vue.demanderEntier("🎯 Coordonnée X de la cible : ");
                int y = vue.demanderEntier("🎯 Coordonnée Y de la cible : ");

                Alien cible = gameManager.attaquerPosition(x, y, degats);
                vue.afficherResultatAttaque(cible, x, y, gameManager.getActiveTeam());

                if (cible != null) {
                    gameManager.skipTurn();
                }
                break;

            case "passer":
                gameManager.skipTurn();
                vue.afficherTourPasse();
                break;

            case "worms":
                vue.afficherTousLesWorms(gameManager);
                break;

            case "map":
                vue.afficherTerrain(gameManager);
                break;

            case "exit":
                enCours = false;
                vue.afficherPartiAbandon();
                break;

            default:
                vue.afficherCommandeInconnue();
                break;
        }
    }
}
