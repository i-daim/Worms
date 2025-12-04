package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileStickyBomb extends Projectile {
    private int timer;
    private boolean stuck; // Indique si la bombe est collée

    public ProjectileStickyBomb(Weapon weapon, Alien shooter) {
        super(weapon, shooter, shooter.getPosition().getX(), shooter.getPosition().getY());
        this.timer = 3 * 60; // 3 secondes avant explosion
        this.stuck = false;
    }
    public void decrementTimer() {
        if (timer > 0) {
            timer--;
        }
    }
    public boolean isReadyToExplode() {
        return timer <= 0;
    }
}
