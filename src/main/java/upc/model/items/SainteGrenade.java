package upc.model.items;
import upc.model.entites.Projectile;
import upc.model.entites.ProjectileGrenade;
import upc.model.entites.Alien;

public class SainteGrenade extends Throwable {
    public static final int DEGATS = 65;
    public static final int RAYON = 10;

    public SainteGrenade(String nom) {
        super(nom, DEGATS, RAYON);
    }
    @Override
    public Projectile createProjectile(Alien shooter) {
        // Sainte Grenade : 5 secondes (Alléluia !)
        return new ProjectileGrenade(this, shooter, 5);
    }
}
