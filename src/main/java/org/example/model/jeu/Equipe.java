package org.example.model.jeu;

import org.example.model.entites.Worm;

public class Equipe {
    private final Worm[] worms;

    public Equipe(int tailleEquipe) {
        this.worms = new Worm[tailleEquipe];
    }

    public Worm[] getWorms() {
        return worms;
    }

}
