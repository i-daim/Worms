package upc.model.items;

import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;

public class Uzi extends Weapon {
    public static final int DEGATS = 2;
    public static final int NBTIR = 25;

    public Uzi() {
        super("Uzi", DEGATS, NBTIR);
    }

    public void useItem(GameWorld gameWorld, Alien shooter) {

    }
}
