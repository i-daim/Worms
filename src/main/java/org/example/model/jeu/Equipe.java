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

    public void ajouterWorm(int index, Worm worm) {
        if (index >= 0 && index < worms.length) {
            worms[index] = worm;
        }
    }

    public int getTailleEquipe() {
        return worms.length;
    }

    public Equipe getEquipe() {
        return this;
    }
}
