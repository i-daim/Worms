package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileGrenade extends Projectile {
    private int timer;
    public ProjectileGrenade(Weapon weapon, Alien shooter, int timerSeconds) {
        super(weapon, shooter, shooter.getPosition().getX(), shooter.getPosition().getY());
        this.timer = timerSeconds * 60;
    }
}
