package org.example.model.entites;

import org.dyn4j.dynamics.Body;
import org.example.model.items.Weapon;

public class Projectile extends Entity {
    public enum ProjectileState {
        FLYING,
        EXPLODED,
        OUT_OF_BOUNDS
      }
    private final Weapon weapon;
    private final Alien shooter;
    private Body body;
    private ProjectileState state;
    private int frameCount;

    public Projectile(Weapon weapon, Alien shooter, int startX, int startY) {
        super(startX, startY, 1);
        this.weapon = weapon;
        this.shooter = shooter;
        this.state = ProjectileState.FLYING;
     }
    public Weapon getWeapon() {
        return weapon;
    }
    public Alien getShooter() {
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
    public int getFrameCount() {
        return frameCount;
    }








}






