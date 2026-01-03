package upc.model.entites.projectiles;

import org.dyn4j.dynamics.Body;
import upc.model.entites.Alien;
import upc.model.entites.Entity;
import upc.model.entites.Position;
import upc.model.items.Weapon;
import upc.model.jeu.GameWorld;

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
    private final Weapon weapon;
    private final int angle;
    private boolean explosionDamageApplied = false;


    /// /    CONSTRUCTOR    ////
    public Projectile(Alien shooter, Position position, int damage,
                      int explosionRadius, boolean explosive, double speed, Weapon weapon) {
        super(position, speed);
        this.shooter = shooter;
        this.damage = damage;
        this.explosionRadius = explosionRadius;
        this.explosive = explosive;
        this.state = State.FLYING;
        this.weapon = weapon;
        this.angle = shooter.getAimAngle();
    }

    public int computeDamageFromDistance(double distance) {
        if (!isExplosive()) {
            return damage;
        }
        double ratio = 1.0 - (distance / explosionRadius);
        return Math.max((int) (damage * ratio), 0);
    }

    public void applyExplosionDamage(GameWorld world) {
        if (!isExplosive() || explosionDamageApplied) {
            return;
        }

        explosionDamageApplied = true;

        Position pos = getPosition();
        for (Alien alien : world.aliensInRadius(pos.getX(), pos.getY(), explosionRadius)) {

            double distance = alien.getPosition().distance(pos.getX(), pos.getY());

            int dmg = computeDamageFromDistance(distance);
            alien.receiveDmg(dmg);
        }
    }

    ////    GETTERS    ////
    public Alien getShooter() {
        return shooter;
    }

    public int getDamage() {
        return damage;
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    public boolean isExplosive() {
        return explosive;
    }

    public boolean isActive() {
        return state == State.FLYING;
    }

    public State getState() {
        return state;
    }

    public Body getBody() {
        return body;
    }

    public Weapon getWeapon() {
        return weapon;
    }
    public int getAngle() {
        return angle;
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














