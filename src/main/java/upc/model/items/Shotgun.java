package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.Projectile;
import upc.model.entites.ProjectileBalle;

import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;

public class Shotgun extends Weapon {
    public Shotgun() {
        super("Shotgun", 35, 8,60);
    }

    public void useItem(GameWorld gameWorld, Alien shooter) {

    }
}
