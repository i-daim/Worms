package org.example.model.jeu;

import org.example.model.entites.Worm;

public class Partie {
    // Constantes
    private static final int TEMPS_DE_JEU = 60; // secondes par tour
    private static final int NOMBRE_WORMS_PAR_EQUIPE = 4;

    // Attributs
    private final Terrain terrain;
    private final Equipe[] equipes;

    // Attributs variables
    private int indexEquipeActive;
    private int tourActuel;
    private int tempsRestant;
    private boolean partieTerminee;

    // Constructeur
    public Partie(int largeurMonde, int hauteurMonde) {
        this.terrain = Terrain.creerTerrainVide(largeurMonde, hauteurMonde);
        this.equipes = new Equipe[2];
        this.equipes[0] = new Equipe("Équipe 1", NOMBRE_WORMS_PAR_EQUIPE);
        this.equipes[1] = new Equipe("Équipe 2", NOMBRE_WORMS_PAR_EQUIPE);
        this.indexEquipeActive = 0;
        this.tourActuel = 1;
        this.tempsRestant = TEMPS_DE_JEU;
        this.partieTerminee = false;

        initialiserPartie();
    }

    private void initialiserPartie() {
        equipes[0].initialiserEquipe(50, 50, 150, 250);
        int xDroite = terrain.getLargeur() - 200;
        equipes[1].initialiserEquipe(xDroite, 50, 150, 250);
    }


    // ========== ACTIONS DE JEU ==========

    public boolean deplacerWormActuelGauche() {
        Worm worm = getWormActif();
        if (worm != null && worm.estVivant()) {
            return worm.deplacerGauche(terrain);
        }
        return false;
    }

    public boolean deplacerWormActuelDroite() {
        Worm worm = getWormActif();
        if (worm != null && worm.estVivant()) {
            return worm.deplacerDroite(terrain);
        }
        return false;
    }

    public boolean infligerDegatsEquipeEnnemie(int degats) {
        if (degats < 0) {
            degats = 0;
        }

        Equipe equipeEnnemie = getEquipeEnnemie();

        for (Worm worm : equipeEnnemie.getWorms()) {
            if (worm != null && worm.estVivant()) {
                worm.subirDegats(degats);
                verifierFinPartie();
                return true;
            }
        }

        return false;
    }
    public void passerTour() {
        finDuTour();
    }

// ========== GESTION DES TOURS ==========

    private void finDuTour() {
        indexEquipeActive = (indexEquipeActive + 1) % equipes.length;
        if (indexEquipeActive == 0) {
            tourActuel++;
        }
        getEquipeActive().passerAuWormSuivant();
        if (getEquipeActive().estEliminee()) {
            partieTerminee = true;
            return;
        }
        tempsRestant = TEMPS_DE_JEU;
    }

    public void decompterTemps() {
        if (tempsRestant > 0) {
            tempsRestant--;
        } else {
            finDuTour();
        }
    }

    private void verifierFinPartie() {
        for (Equipe equipe : equipes) {
            if (equipe.estEliminee()) {
                partieTerminee = true;
                return;
            }
        }
    }

    // ========== GETTERS ==========

    public Terrain getTerrain() {
        return terrain;
    }
    public Equipe getEquipeActive() {
        return equipes[indexEquipeActive];
    }
    public Equipe getEquipeEnnemie() {
        int indexEnnemi = (indexEquipeActive + 1) % equipes.length;
        return equipes[indexEnnemi];
    }


    public void reinitialiser() {
        tourActuel = 0;
        tempsRestant = TEMPS_DE_JEU;
        partieTerminee = false;
        initialiserPartie();
    }

    public void pauserPartie() {
    }

    public void reprendrePartie() {
    }

    // ========== GETTERS ==========

    public Terrain getTerrain() {
        return terrain; }

    public Equipe getEquipe1() {
        return equipe1;
    }

    public Equipe getEquipe2() {
        return equipe2;
    }

    public int getTempsRestant() {
        return tempsRestant;
    }

    public static int getTourActuel() {
        return tourActuel;
    }

    public Worm getWormActuel() {
        return wormActuel;
    }

    public Equipe getEquipeActive() {
        return equipeActive;
    }

    public boolean estTerminee() {
        return partieTerminee;
    }

    public static int getTempsDeJeu() {
        return TEMPS_DE_JEU;
    }

    // ========== SETTERS  ==========

    public void setTempsRestant(int temps) {
        this.tempsRestant = temps;
    }

    public void setWormActuel(Worm worm) {
        if (wormActuel != null) {
            wormActuel.setActif(false);
        }
        this.wormActuel = worm;
        if (worm != null) {
            worm.setActif(true);
        }
    }

    // ========== MÉTHODES UTILES ==========

    public int getNombreWormsVivants() {
        int count = 0;

        for (Worm w : equipe1.getWorms()) {
            if (w != null && w.estVivant()) {
                count++;
            }
        }

        for (Worm w : equipe2.getWorms()) {
            if (w != null && w.estVivant()) {
                count++;
            }
        }

        return count;
    }

    public String toString() {
        return "Partie [Tour: " + tourActuel
                + ", Temps: " + tempsRestant
                + "s, Worm actif: "
                + (wormActuel != null ? wormActuel : "Aucun")
                + ", Équipe active: " + (equipeActive == equipe1 ? "1" : "2")
                + ", Terminée: " + partieTerminee + "]";
    }
}
