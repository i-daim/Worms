package upc.model.items;
import upc.model.entites.Alien;

import upc.model.jeu.GameWorld;

public class Grenade extends Weapon {
    public Grenade() {
        super("Grenade", 50, 3, 50);
    }

    public void useItem(GameWorld gameWorld, Alien shooter) {

    }
}
