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

        // Faire tomber les worms et afficher les dégâts
        simulerChuteInitiale();

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


    private void simulerChuteInitiale() {
        vue.afficherChute();

        // Sauvegarder les HP initiaux
        int[][] hpAvant = sauvegarderHP();

        // Simuler la chute (3 secondes à 60 FPS)
        for (int i = 0; i < 180; i++) {
            partie.update(1.0 / 60.0);
        }

        // Afficher les résultats via la Vue
        vue.afficherDegatsDeChuteInitiaux(partie, hpAvant);
    }


    private int[][] sauvegarderHP() {
        int[][] hpAvant = new int[2][2];
        for (int i = 0; i < 2; i++) {
            Worm[] worms = partie.getTeam(i).getWorms();
            for (int j = 0; j < worms.length; j++) {
                if (worms[j] != null) {
                    hpAvant[i][j] = worms[j].getHealthPoints();
                }
            }
        }
        return hpAvant;
    }


    private void simulerSaut(Worm worm) {
        int hpAvant = worm.getHealthPoints();

        partie.getMonde().getPhysicsEngine().jumpWorm(worm);

        vue.afficherSautEnCours();

        for (int i = 0; i < 120; i++) {
            partie.update(1.0 / 60.0);
        }

        int degats = hpAvant - worm.getHealthPoints();
        vue.afficherDegatsChuteApresAction(degats);
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
                simulerSaut(partie.getActiveWorm());
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
