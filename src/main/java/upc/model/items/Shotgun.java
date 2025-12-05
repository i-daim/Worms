package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.Projectile;
import upc.model.entites.ProjectileBalle;

import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;

public class Shotgun extends Weapon {
    public static final int DEGATS = 25;
    public static final int NBTIR = 2;

    public Shotgun() {
        super("Shotgun", DEGATS, NBTIR);
    }

    public void useItem(GameWorld gameWorld, Alien shooter) {

    }
    @Override
    public Projectile createProjectile(Alien shooter) {
        return new ProjectileBalle(this, shooter);
    }
}
