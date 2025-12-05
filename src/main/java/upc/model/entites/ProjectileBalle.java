package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileBalle extends Projectile {
    public ProjectileBalle(Weapon weapon, Alien shooter) {
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
