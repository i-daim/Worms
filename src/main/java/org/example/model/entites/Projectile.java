package org.example.model.entites;

import org.dyn4j.dynamics.Body;
import org.example.model.items.Arme;

public class Projectile extends Entite {
    public enum State {
        FLYING,
        EXPLODED,
        OUT_OF_BOUNDS
      }
    private final Arme weapon;
    private final Worm shooter;
    private Body body;
    private State state;

    public Projectile(Arme weapon, Worm shooter, int startX, int startY) {
        super(startX, startY, 1);
        this.weapon = weapon;
        this.shooter = shooter;
        this.state = State.FLYING;
     }
    public Arme getWeapon() {
        return weapon;
    }
    public Worm getShooter() {
        return shooter;
    }
    public Body getBody() {
        return body;
    }
    public void setBody(Body body) {
        this.body = body;
    }







}






