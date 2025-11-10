package org.example.model.jeu;

import org.example.model.entites.Worm;

public class Partie {
    // Constantes
    private static final int TEMPS_DE_JEU = 60; // secondes par tour
    private static final int NOMBRE_WORMS_PAR_EQUIPE = 4;

    private static int tourActuel = 0;

    // Attributs
    private final Terrain terrain;
    private final Equipe equipe1;
    private final Equipe equipe2;

    // Attributs variables
    private int tempsRestant;
    private Worm wormActuel;
    private Equipe equipeActive;
    private boolean partieTerminee;
    private boolean actif;

    // Constructeur
    public Partie(int largeurMonde, int hauteurMonde) {
        this.terrain = Terrain.creerTerrainVide(largeurMonde, hauteurMonde);
        this.equipe1 = new Equipe("equipe1", NOMBRE_WORMS_PAR_EQUIPE);
        this.equipe2 = new Equipe("equipe2", NOMBRE_WORMS_PAR_EQUIPE);
        this.tempsRestant = TEMPS_DE_JEU;
        this.partieTerminee = false;

        initialiserPartie();
    }

    /**
     * Initialise la partie : crée les worms et les positionne
     */
    private void initialiserPartie() {
        // Initialiser équipe 1 (gauche)
        initialiserEquipe(equipe1, 100, "Equipe 1");

        // Initialiser équipe 2 (droite)
        initialiserEquipe(equipe2, terrain.getLargeur() - 100, "Equipe 2");

        // Première équipe commence
        equipeActive = equipe1;
        changerWormActif();
    }

    /**
     * Initialise une équipe avec ses worms
     */
    private void initialiserEquipe(Equipe equipe, int positionX,
                                   String nomEquipe) {
        Worm[] worms = equipe.getWorms();

        for (int i = 0; i < worms.length; i++) {
            // Créer le worm à une position
            int x = positionX + (i * 30);
            int y = monde.getHauteur() / 2 + (i * 50);

            worms[i] = new Worm(100, equipe, x, y); // 100 points de vie

            // Faut ajouter une méthode ajouter au monde
        }
    }

    /**
     * Passe au tour suivant
     */
    public void tourSuivant() {
        tourActuel++;

        // Alterner entre équipes
        if (equipeActive == equipe1) {
            equipeActive = equipe2;
        } else {
            equipeActive = equipe1;
        }

        // Changer de worm actif
        changerWormActif();

        // Réinitialiser le temps
        tempsRestant = TEMPS_DE_JEU;
    }

    private void changerWormActif() {
        Worm[] worms = equipeActive.getWorms();

        // Trouver le prochain worm vivant
        for (Worm worm : worms) {
            if (worm != null && worm.estVivant()) {
                wormActuel = worm;
                wormActuel.setActif(true);
                return;
            }
        }

        // Si aucun worm vivant, l'équipe a perdu
        wormActuel = null;
    }

    public void mettreAJour(double deltaTime) {
        if (partieTerminee) {
            return;
        }
        // faudrait ajouter une méthode mettre ajour le monde

        // Gérer le temps restant
        tempsRestant -= deltaTime;

        if (tempsRestant <= 0) {
            // Temps écoulé, passer au tour suivant
            finDuTour();
        }

        // Vérifier si la partie est terminée
        verifierFinPartie();
    }

    public void finDuTour() {
        if (wormActuel != null) {
            wormActuel.setActif(false);
        }
        tourSuivant();
    }

    private void verifierFinPartie() {
        boolean equipe1Eliminee = equipe1.estEliminee();
        boolean equipe2Eliminee = equipe2.estEliminee();

        if (equipe1Eliminee || equipe2Eliminee) {
            partieTerminee = true;
        }
    }

    public Equipe getEquipeGagnante() {
        if (!partieTerminee) {
            return null;
        }

        if (equipe1.estEliminee()) {
            return equipe2;
        } else {
            return equipe1;
        }
    }

    public void decompterTemps() {
        if (tempsRestant > 0) {
            tempsRestant--;
        } else {
            finDuTour();
        }
    }

    /**
     * Réinitialise la partie
     */
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

    public Monde getMonde() {
        return monde;
    }

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
