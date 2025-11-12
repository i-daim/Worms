package org.example.model.jeu;

import org.example.model.entites.Worm;

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

    public void initialiserEquipe(int pointsDeVie, int positionX, int positionY) {
        for (int i = 0; i < worms.length; i++) {
            // TODO : Trouver un moyen de "spawn" les Worms dans une position aléatoire (et valide)
            worms[i] = new Worm(pointsDeVie, this,  positionX, positionY);
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
