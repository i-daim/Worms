package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileExplosifContact extends Projectile {
    public ProjectileExplosifContact(Weapon weapon, Alien shooter, int startX, int startY) {
        super(weapon, shooter, startX, startY);
    }
}
