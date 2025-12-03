package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileMine extends Projectile {
    private int timer;
    private boolean armed;

    public ProjectileMine(Weapon weapon, Alien shooter) {
        super(weapon, shooter, shooter.getPosition().getX(), shooter.getPosition().getY());
        this.timer = 5 * 60; // 5 secondes avant explosion (ou armement)
        this.armed = false;
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
