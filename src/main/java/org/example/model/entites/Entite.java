package org.example.model.entites;

public abstract class Entite {
    private int positionX;
    private int positionY;

    public Entite(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
