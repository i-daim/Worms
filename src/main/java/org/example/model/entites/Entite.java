package org.example.model.entites;

public abstract class Entite {
    private Position position;
    private int speed;

    public Entite(int x, int y, int speed) {
        this.position = new Position(x, y);
        this.speed = speed;
    }

    public Position getPosition() {
        return position;
    }

    public int getSpeed() {
        return speed;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
