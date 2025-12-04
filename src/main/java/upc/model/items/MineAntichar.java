package upc.model.items;
import upc.model.entites.ProjectileMine;
import upc.model.entites.Projectile;
import upc.model.entites.Alien;

public class MineAntichar extends Trap {
    public static final int DEGATS = 60;
    public static final int RAYON = 3;
    public MineAntichar(String nom) {
        super(nom, DEGATS, RAYON);
    }
    @Override
    public Projectile createProjectile(Alien shooter) {
        return new ProjectileMine(this, shooter);
    }
}
