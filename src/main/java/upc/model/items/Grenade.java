package upc.model.items;
import upc.model.entites.Projectile;
import upc.model.entites.ProjectileGrenade;
import upc.model.entites.Alien;

import upc.model.jeu.GameWorld;

public class Grenade extends Weapon {
    public static final int DEGATS = 45;
    public static final int RAYON = 5;

    public Grenade() {
        super("Grenade", DEGATS, 1);
    }

    public void useItem(GameWorld gameWorld, Alien shooter) {

    }
}
