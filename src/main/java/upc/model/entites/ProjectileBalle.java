package upc.model.entites;
import upc.model.items.Weapon;

public class ProjectileBalle extends Projectile {
    public ProjectileBalle(Weapon weapon, Alien shooter) {
        super(weapon, shooter, shooter.getPosition().getX(), shooter.getPosition().getY());
    }
}
