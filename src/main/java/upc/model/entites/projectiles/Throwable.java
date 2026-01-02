package upc.model.entites.projectiles;
import upc.model.entites.Alien;
import upc.model.items.Weapon;

public class Throwable extends Projectile {

    private int timer;

    public Throwable(Weapon weapon, Alien shooter, int timerSeconds, int radius) {
        super(
                shooter,
                shooter.getPosition(),
                weapon.getDamage(),
                radius,
                true,
                weapon.getSpeed(),
                weapon
        );
        this.timer = timerSeconds * 60;
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
