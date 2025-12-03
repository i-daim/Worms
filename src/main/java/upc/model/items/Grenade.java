package upc.model.items;
import upc.model.entites.Projectile;
import upc.model.entites.ProjectileGrenade;
import upc.model.entites.Alien;

public class Grenade extends Throwable {
    public static final int DEGATS = 45;
    public static final int RAYON = 5;

    public Grenade(String nom) {
        super(nom, DEGATS, RAYON);
    }
    @Override
    public Projectile createProjectile(Alien shooter) {
        return new ProjectileGrenade(this, shooter, 3);
    }

}
