package org.example.controller;

import org.example.model.jeu.Partie;
import org.example.model.jeu.Terrain;
import org.example.view.console.VueConsole;

public class ControllerConsole {
    private final Partie partie;
    private final VueConsole vue;
    private boolean enCours;

    public ControllerConsole(Terrain terrain) {
        this.partie = new Partie(terrain);
        this.vue = new VueConsole();
        this.enCours = true;
    }

    public ControllerConsole(int largeur, int hauteur) {
        this(Terrain.creerTerrainVide(largeur, hauteur));
    }

    public void demarrer() {
        vue.afficherBienvenue();
        vue.afficherTerrain(partie.getTerrain());

        while (enCours && !partie.estTerminee()) {
            vue.afficherEtatPartie(partie);
            String commande = vue.demanderCommande();
            traiterCommande(commande);
        }

        if (partie.estTerminee()) {
            vue.afficherFinPartie(partie);
        }

        vue.close();
    }

    private void traiterCommande(String commande) {
        switch (commande) {
            case "gauche":
                vue.afficherResultatDeplacement(partie.deplacerWormActuelGauche(), "gauche");
                break;

            case "droite":
                vue.afficherResultatDeplacement(partie.deplacerWormActuelDroite(), "droite");
                break;

            case "sauter":
                vue.afficherResultatSaut(partie.sauterWormActuel());
                break;

            case "degats":
                int degats = vue.demanderEntier("💥 Dégâts ? ");
                vue.afficherResultatDegats(partie.infligerDegatsEquipeEnnemie(degats), degats);
                break;

            case "passer":
                partie.passerTour();
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
