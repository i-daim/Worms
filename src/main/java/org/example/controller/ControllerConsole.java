package org.example.controller;

import org.example.model.jeu.Partie;
import org.example.view.console.VueConsole;

public class ControllerConsole {
    private final Partie partie;
    private final VueConsole vue;
    private boolean enCours;

    public ControllerConsole(int largeur, int hauteur) {
        this.partie = new Partie(largeur, hauteur);
        this.vue = new VueConsole();
        this.enCours = true;
    }

    public void demarrer() {
        vue.afficherBienvenue();

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
                boolean succesGauche = partie.deplacerWormActuelGauche();
                vue.afficherResultatDeplacement(succesGauche, "gauche");
                break;

            case "droite":
                boolean succesDroite = partie.deplacerWormActuelDroite();
                vue.afficherResultatDeplacement(succesDroite, "droite");
                break;

            case "sauter":
                boolean succesSaut = partie.sauterWormActuel();
                vue.afficherResultatSaut(succesSaut);
                break;

            case "degats":
                int degats = vue.demanderEntier("💥 Combien de dégâts ? ");
                boolean degatsInfliges = partie.infligerDegatsEquipeEnnemie(degats);
                vue.afficherResultatDegats(degatsInfliges, degats);
                break;

            case "passer":
                partie.passerTour();
                vue.afficherTourPasse();
                break;

            case "info":
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
