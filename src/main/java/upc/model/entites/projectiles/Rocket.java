package upc.model.entites.projectiles;
import upc.model.entites.Alien;
import upc.model.items.Weapon;
import  upc.model.entites.Position;

public class Rocket extends Projectile {
    private static final int EXPLOSION_RADIUS = 30;

    public Rocket(Weapon weapon, Alien shooter, Position startPos) {
        super(
                shooter,
                startPos,
                weapon.getDamage(),
                EXPLOSION_RADIUS,
                true,
                weapon.getBasePower(),
                weapon
        );
    }
}
