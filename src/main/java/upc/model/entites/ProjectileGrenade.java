package upc.model.entites;
import upc.model.items.Weapon;


public class ProjectileGrenade extends Projectile {
    private int timer;
    public ProjectileGrenade(Weapon weapon, Alien shooter, int timerSeconds) {
        super(
                shooter,
                shooter.getPosition(),
                weapon.getDamage(),
                50,
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
