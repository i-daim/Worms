package org.example.model.jeu;

import org.example.model.entites.Worm;

import java.util.Random;

public class Equipe {
    private final String nom;
    private final Worm[] worms;
    private int indexWormActif;

    public Equipe(String nom, int tailleEquipe) {
        this.nom = nom;
        this.worms = new Worm[tailleEquipe];
        this.indexWormActif = 0;
    }

    public String getNom() {
        return nom;
    }

    public Worm[] getWorms() {
        return worms;
    }

    public int getIndexWormActif() {
        return indexWormActif;
    }

    public Worm getWormActif() {
        if (indexWormActif < worms.length && worms[indexWormActif] != null) {
            return worms[indexWormActif];
        }
        return null;
    }

    public void initialiserEquipe(int zoneX, int zoneY, int largeurZone, int hauteurZone) {
        Random random = new Random();

        for (int i = 0; i < worms.length; i++) {
            // Position aléatoire dans la zone assignée
            int x = zoneX + random.nextInt(largeurZone);
            int y = zoneY + random.nextInt(hauteurZone);

            worms[i] = new Worm(this, x, y);
        }
    }

    public boolean estEliminee() {
        for (Worm w : worms) {
            if (w != null && w.estVivant()) {
                return false; // il reste au moins un ver vivant
            }
        }
        return true; // tous les vers sont morts
    }


}
