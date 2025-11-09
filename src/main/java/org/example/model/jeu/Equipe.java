package org.example.model.jeu;

import org.example.model.entites.Worm;

public class Equipe {
    private final String nom;
    private final Worm[] worms;

    public Equipe(String nom, int tailleEquipe) {
        this.nom = nom;
        this.worms = new Worm[tailleEquipe];
    }

    public String getNom() {
        return nom;
    }

    public Worm[] getWorms() {
        return worms;
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
