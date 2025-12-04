package upc.model.items;
import upc.model.entites.ProjectileMine;
import upc.model.entites.Projectile;
import upc.model.entites.Alien;

public class Mine extends Trap {
    public static final int DEGATS = 45;
    public static final int RAYON = 3;
    public Mine(String nom) {
        super(nom, DEGATS, RAYON);
    }
    @Override
    public Projectile createProjectile(Alien shooter) {
        return new ProjectileMine(this, shooter);
    }
}
