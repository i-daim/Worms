package org.example.model.entites;

import org.example.model.jeu.Equipe;

public class Worm extends Entite {
    private final Equipe equipe;
    private int pointsDeVie;

    public Worm(int pointsDeVie, Equipe equipe, int positionX, int positionY) {
        super(positionX, positionY);
        this.pointsDeVie = pointsDeVie;
        this.equipe = equipe;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public Equipe getEquipe() {
        return equipe;
    }
}
