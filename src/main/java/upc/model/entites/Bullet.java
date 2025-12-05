package upc.model.entites;
import upc.model.items.Weapon;

public class Bullet extends Projectile {
    public Bullet(Weapon weapon, Alien shooter) {
        super(
                shooter,
                shooter.getPosition(),
                weapon.getDamage(),
                0,
                false,
                weapon.getSpeed(),
                weapon
        );
    }
}
