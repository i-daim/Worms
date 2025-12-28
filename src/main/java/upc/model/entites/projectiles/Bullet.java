package upc.model.entites.projectiles;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.items.Weapon;

public class Bullet extends Projectile {

    public Bullet(Weapon weapon, Alien shooter, Position startPos) {
        super(
                shooter,
                startPos,
                weapon.getDamage(),
                0,
                false,
                weapon.getSpeed(),
                weapon
        );
    }
}
