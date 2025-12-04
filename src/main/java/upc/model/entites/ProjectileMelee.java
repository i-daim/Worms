package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileMelee extends Projectile{
    public ProjectileMelee(Weapon weapon, Alien shooter) {
        super(weapon, shooter,
                shooter.getPosition().getX() + (shooter.getDirection() * 20),
                shooter.getPosition().getY());
    }
}
