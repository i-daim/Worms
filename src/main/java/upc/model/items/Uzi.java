package upc.model.items;
import upc.model.entites.Projectile;
import upc.model.entites.ProjectileBalle;
import upc.model.entites.Alien;

public class Uzi extends Firearm {
    public static final int DEGATS = 2;
    public static final int RAYON = 10;
    public static final int NBTIR = 25;

    public Uzi(String nom) {
        super(nom, DEGATS, RAYON, NBTIR);
    }
    @Override
    public Projectile createProjectile(Alien shooter) {
        return new ProjectileBalle(this, shooter);
    }
}
