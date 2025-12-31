package upc.model.entites.projectiles;

import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.items.Weapon;

public class UziBullet extends Bullet {

    public UziBullet(Weapon weapon, Alien shooter, Position startPos) {
        super(weapon, shooter, startPos);
    }
}