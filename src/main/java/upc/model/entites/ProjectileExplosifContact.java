package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileExplosifContact extends Projectile {
    public ProjectileExplosifContact(Weapon weapon, Alien shooter) {
        super(weapon, shooter, shooter.getPosition().getX(), shooter.getPosition().getY());
    }
}
