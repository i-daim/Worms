package org.example.controller;

import org.example.model.entites.Worm;
import org.example.model.jeu.Partie;
import org.example.view.console.VueConsole;

public class ControllerConsole {
    private final Partie partie;
    private final VueConsole vue;
    private boolean enCours;

    public ControllerConsole() {
        this.partie = new Partie("");
        this.vue = new VueConsole();
        this.enCours = true;
    }

    public void demarrer() {
        vue.afficherBienvenue();
        vue.afficherTerrain(partie.getTerrain());

        // Faire tomber les worms
        System.out.println("\n⏳ Les worms tombent...\n");

        for (int i = 0; i < 180; i++) {
            partie.update(1.0 / 60.0);
        }

        // Afficher l'état après la chute
        vue.afficherEtatApresChute(partie);

        while (enCours && !partie.isOver()) {
            vue.afficherEtatPartie(partie);
            String commande = vue.demanderCommande();
            traiterCommande(commande);
        }

        if (partie.isOver()) {
            vue.afficherFinPartie(partie);
        }

        vue.close();
    }

    private void traiterCommande(String commande) {
        switch (commande) {
            case "gauche":
                boolean reussiteG = partie.getActiveWorm().moveLeft(partie.getTerrain());
                vue.afficherResultatDeplacement(reussiteG, "gauche");
                if (reussiteG) {
                    partie.skipTurn();
                }
                break;

            case "droite":
                boolean reussiteD = partie.getActiveWorm().moveRight(partie.getTerrain());
                vue.afficherResultatDeplacement(reussiteD, "droite");
                if (reussiteD) {
                    partie.skipTurn();
                }
                break;

            case "sauter":
                partie.getMonde().getPhysicsEngine().jumpWorm(partie.getActiveWorm());

                System.out.println("⏳ Le worm saute...");
                for (int i = 0; i < 120; i++) {
                    partie.update(1.0 / 60.0);
                }

                vue.afficherResultatSaut(true);
                partie.skipTurn();
                break;

            case "attaquer":
                int degats = vue.demanderEntier("💥 Combien de dégâts ? ");
                int x = vue.demanderEntier("🎯 Coordonnée X de la cible : ");
                int y = vue.demanderEntier("🎯 Coordonnée Y de la cible : ");

                Worm cible = partie.attaquerPosition(x, y, degats);
                vue.afficherResultatAttaque(cible, x, y, partie.getActiveTeam());

                if (cible != null) {
                    partie.skipTurn();
                }
                break;

            case "passer":
                partie.skipTurn();
                vue.afficherTourPasse();
                break;

            case "worms":
                vue.afficherTousLesWorms(partie);
                break;

            case "map":
                vue.afficherTerrain(partie.getTerrain());
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
