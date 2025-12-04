package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.Projectile;
import upc.model.entites.ProjectileBalle;

public class Shotgun extends Firearm {
    public static final int DEGATS = 25;
    public static final int RAYON = 5;
    public static final int NBTIR = 2;

    public Shotgun(String nom) {
        super(nom, DEGATS, RAYON, NBTIR);
    }
    @Override
    public Projectile createProjectile(Alien shooter) {
        return new ProjectileBalle(this, shooter);
    }
}
