package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileBazooka extends Projectile {
    public ProjectileBazooka(Weapon weapon, Alien shooter) {
        super(
                shooter,
                shooter.getPosition(),
                weapon.getDamage(),
                45,
                true,
                weapon.getPuissanceTir(),
                weapon
        );
    }
}
