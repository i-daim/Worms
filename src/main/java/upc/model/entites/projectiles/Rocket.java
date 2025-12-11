package upc.model.entites.projectiles;
import upc.model.entites.Alien;
import upc.model.items.Weapon;

public class Rocket extends Projectile {
    public Rocket(Weapon weapon, Alien shooter) {
        super(
                shooter,
                shooter.getPosition(),
                weapon.getDamage(),
                45,
                true,
                weapon.getSpeed(),
                weapon
        );
    }
}
