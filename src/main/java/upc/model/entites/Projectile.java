package upc.model.entites;

import org.dyn4j.dynamics.Body;
import upc.model.items.Weapon;

public class Projectile extends Entity {

    public enum Type {
        BULLET,
        GRENADE,
        ROCKET
    }

    public enum State {
        FLYING,
        EXPLODED,
        OUT_OF_BOUNDS
    }

    private final Alien shooter;

    private Body body;
    private State state;
    private boolean explosive;
    private int frameCount;


////    CONSTRUCTOR    ////
    public Projectile(Weapon weapon, Alien shooter, Position position, double speed) {
        super(position, speed);
        this.shooter = shooter;
        this.state = State.FLYING;
     }


////    GETTERS    ////
    public Alien getShooter() {
        return shooter;
    }
    public Body getBody() {
        return body;
    }
    public boolean isActive() {
        return state == State.FLYING;
    }
    public State getState() {
        return state;
    }

////    SETTERS    ////
    public void setBody(Body body) {
        this.body = body;
    }
    public void setState(State state) {
        this.state = state;
    }
    public void incrementFrame() {
        frameCount++;
    }
    public int getFrameCount() {
        return frameCount;
    }








}






