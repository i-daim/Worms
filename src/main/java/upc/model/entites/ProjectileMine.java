package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileMine extends Projectile {
    private int timer;
    private boolean armed;

    public ProjectileMine(Weapon weapon, Alien shooter) {
        super(
                shooter,
                shooter.getPosition(),
                weapon.getDamage(),
                60,
                true,
                weapon.getSpeed(),
                weapon
        );
        this.timer = 5 * 60;
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
