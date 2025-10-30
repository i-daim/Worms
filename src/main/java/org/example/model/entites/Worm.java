package org.example.model.entites;
import org.example.model.jeu.Equipe;

public class Worm extends Entite {
    private int pointsDeVie;
    public final Equipe equipe;

    public Worm(int pointsDeVie, Equipe equipe, int positionX, int positionY) {
        super(positionX, positionY);
        this.pointsDeVie = pointsDeVie;
        this.equipe = equipe;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }
}
