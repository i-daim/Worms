package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileBalle extends Projectile {
    public ProjectileBalle(Weapon weapon, Alien shooter, int startX, int startY) {
        super(weapon, shooter, startX, startY);
    }
}
