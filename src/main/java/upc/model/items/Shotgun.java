package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Bullet;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Projectile;

import upc.model.jeu.GameWorld;

public class Shotgun extends Weapon {
    public Shotgun() {
        super("Shotgun", 35, 8, 60);
    }

    public void useItem(GameWorld gameWorld, Alien shooter) {

    }
    @Override
    public Projectile createProjectile(Alien shooter, Position startPosition, int angleDegrees, int powerPercent) {
        return new Bullet(this, shooter);
    }
}
