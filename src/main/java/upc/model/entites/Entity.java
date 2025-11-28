package upc.model.entites;

public abstract class Entity {
    private Position position;
    private int speed;

    public Entity(int x, int y, int speed) {
        this.position = new Position(x, y);
        this.speed = speed;
    }
    protected Entity() {
        this.position = new Position(0, 0);
        this.speed = 0;
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
