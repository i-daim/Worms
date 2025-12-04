package upc.model.entites;

import org.dyn4j.dynamics.Body;

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
    private final int damage;
    private final int explosionRadius;
    private final boolean explosive;

    private Body body;
    private State state;
    private int frameCount;


////    CONSTRUCTOR    ////
    public Projectile(Alien shooter, Position position, int damage,
                      int explosionRadius, boolean explosive, double speed) {
        super(position, speed);
        this.shooter = shooter;
        this.damage = damage;
        this.explosionRadius = explosionRadius;
        this.explosive = explosive;
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






