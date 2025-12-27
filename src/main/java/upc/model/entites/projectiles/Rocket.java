package upc.model.entites.projectiles;
import upc.model.entites.Alien;
import upc.model.items.Weapon;
import  upc.model.entites.Position;

public class Rocket extends Projectile {

    public Rocket(Weapon weapon, Alien shooter, Position startPos) {
        super(
                shooter,
                startPos,
                weapon.getDamage(),
                45,
                true,
                weapon.getSpeed(),
                weapon
        );
    }
}
