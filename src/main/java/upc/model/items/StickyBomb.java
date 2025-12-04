package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.Projectile;
import upc.model.entites.ProjectileStickyBomb;


public class StickyBomb extends Throwable {
    public static final int DEGATS = 25;
    public static final int RAYON = 5;

    public StickyBomb(String nom) {
        super(nom, DEGATS, RAYON);
    }
    @Override
    public Projectile createProjectile(Alien shooter) {
        return new ProjectileStickyBomb(this, shooter);
    }
}
