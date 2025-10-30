package org.example.model.entites;

public class Worm extends Entite {
    private int pointsDeVie;

    public Worm(int pointsDeVie, int positionX, int positionY) {
        super(positionX, positionY);
        this.pointsDeVie = pointsDeVie;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }
}
