package org.example.model.entites;

public abstract class Entite {
    private Position position;

    public Entite(int x, int y) {
        this.position = new Position(x, y);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
