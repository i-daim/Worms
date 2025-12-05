package upc.model.entites;

public abstract class Entity {
    private Position position;
    private double speed;

//// CONSTRUCTORS ////
    public Entity(Position position, double speed) {
        this.position = position;
        this.speed = speed;
    }

////    GETTERS    ////
    public Position getPosition() {
        return position;
    }

    public double getSpeed() {
        return speed;
    }

////    SETTERS    ////
    public void setPosition(Position position) {
        this.position = position;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
