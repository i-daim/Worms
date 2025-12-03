package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.Projectile;
import upc.model.entites.ProjectileExplosifContact;

public class MiniCanon extends Firearm {
    public static final int DEGATS = 5;
    public static final int RAYON = 10;
    public static final int NBTIR = 50;

    public MiniCanon(String nom) {
        super(nom, DEGATS, RAYON, NBTIR);
    }
    @Override
    public Projectile createProjectile(Alien shooter) {
        return new ProjectileExplosifContact(this, shooter);
    }
}
