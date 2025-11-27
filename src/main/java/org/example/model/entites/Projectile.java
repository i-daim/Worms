package org.example.model.entites;

import org.dyn4j.dynamics.Body;
import org.example.model.items.Arme;

public class Projectile extends Entite {
    public enum ProjectileState {
        FLYING,
        EXPLODED,
        OUT_OF_BOUNDS
      }
    private final Arme weapon;
    private final Worm shooter;
    private Body body;
    private ProjectileState state;
    private int frameCount;

    public Projectile(Arme weapon, Worm shooter, int startX, int startY) {
        super(startX, startY, 1);
        this.weapon = weapon;
        this.shooter = shooter;
        this.state = ProjectileState.FLYING;
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
    public ProjectileState getState() {
        return state;
    }
    public void setState(ProjectileState state) {
        this.state = state;
    }
    public boolean isActive() {
        return state == ProjectileState.FLYING;
    }
    public void incrementFrame() {
        frameCount++;
    }








}






