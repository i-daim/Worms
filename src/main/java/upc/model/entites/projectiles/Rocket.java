package upc.model.entites.projectiles;
import upc.model.entites.Alien;
import upc.model.items.Weapon;
import  upc.model.entites.Position;

public class Rocket extends Projectile {
    public Rocket(Weapon weapon, Alien shooter) {
        super(
                shooter,
                (shooter != null) ? shooter.getPosition() : new Position(0, 0),
                weapon.getDamage(),
                45,
                true,
                weapon.getSpeed(),
                weapon
        );
    }
    public Rocket(Alien shooter, Position startPosition, int damage,
                  int explosionRadius, boolean explosive, double speed, Weapon weapon) {
        super(
                shooter,
                startPosition,
                damage,
                explosionRadius,
                explosive,
                speed,
                weapon
        );
    }
}
