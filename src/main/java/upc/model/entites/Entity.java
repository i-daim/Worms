package upc.model.entites;

public abstract class Entity {
    private Position position;
    private int speed;

//// CONSTRUCTORS ////
    public Entity(Position position, double speed) {
        this.position = position;
        this.speed = speed;
    }

////    GETTERS    ////
    public Position getPosition() {
        return position;
    }

    public int getSpeed() {
        return speed;
    }

////    SETTERS    ////
    public void setPosition(Position position) {
        this.position = position;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
