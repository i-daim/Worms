package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.Projectile;
import upc.model.entites.ProjectileMelee;

public class PiqueVer extends Firearm {
    public static final int DEGATS = 20;
    public static final int RAYON = 10;
    public static final int NBTIR = 5;

    public  PiqueVer(String nom) {
        super(nom, DEGATS, RAYON, NBTIR);
    }
    @Override
    public Projectile createProjectile(Alien shooter) {
        return new ProjectileMelee(this, shooter);
    }
}
