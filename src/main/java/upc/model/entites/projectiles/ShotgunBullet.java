package upc.model.entites.projectiles;

import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.items.Weapon;

public class ShotgunBullet extends Bullet {

    public ShotgunBullet(Weapon weapon, Alien shooter, Position startPos) {
        super(weapon, shooter, startPos);
    }
}