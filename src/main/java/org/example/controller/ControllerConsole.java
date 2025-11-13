package org.example.controller;

import org.example.model.entites.Worm;
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
                boolean reussiteG = partie.deplacerWormActuelGauche();
                vue.afficherResultatDeplacement(reussiteG, "gauche");
                if (reussiteG) {
                    partie.passerTour();
                }
                break;

            case "droite":
                boolean reussiteD = partie.deplacerWormActuelDroite();
                vue.afficherResultatDeplacement(reussiteD, "droite");
                if (reussiteD) {
                    partie.passerTour();
                }
                break;

            case "sauter":
                boolean reussiteS = partie.sauterWormActuel();
                vue.afficherResultatSaut(reussiteS);
                if (reussiteS) {
                    partie.passerTour();
                }
                break;

            case "attaquer":
                int degats = vue.demanderEntier("💥 Combien de dégâts ? ");
                int x = vue.demanderEntier("🎯 Coordonnée X de la cible : ");
                int y = vue.demanderEntier("🎯 Coordonnée Y de la cible : ");

                Worm cible = partie.attaquerPosition(x, y, degats);
                vue.afficherResultatAttaque(cible, x, y, partie.getEquipeActive());

                if (cible != null) {
                    partie.passerTour();
                }
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
