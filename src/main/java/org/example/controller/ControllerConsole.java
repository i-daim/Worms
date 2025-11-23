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
                    partie.skipRound();
                }
                break;

            case "droite":
                boolean reussiteD = partie.getActiveWorm().moveRight(partie.getTerrain());
                vue.afficherResultatDeplacement(reussiteD, "droite");
                if (reussiteD) {
                    partie.skipRound();
                }
                break;

            case "sauter":
                boolean reussiteS = partie.getActiveWorm().jump(partie.getTerrain());
                vue.afficherResultatSaut(reussiteS);
                if (reussiteS) {
                    partie.skipRound();
                }
                break;

            case "attaquer":
                int degats = vue.demanderEntier("💥 Combien de dégâts ? ");
                int x = vue.demanderEntier("🎯 Coordonnée X de la cible : ");
                int y = vue.demanderEntier("🎯 Coordonnée Y de la cible : ");

                Worm cible = partie.attaquerPosition(x, y, degats);
                vue.afficherResultatAttaque(cible, x, y, partie.getActiveTeam());

                if (cible != null) {
                    partie.skipRound();
                }
                break;

            case "passer":
                partie.skipRound();
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
        }
    }
}
