package upc.model.items;

import upc.model.entites.Alien;
import upc.model.entites.Trap;
import upc.model.jeu.GameWorld;

public class Mine extends Weapon {

    public static final int DAMAGE = 45;
    public static final int EXPLOSION_RADIUS = 3;
    public static final int ACTIVATION_RADIUS = 3;

    public Mine() {
        super("Mine", DEGATS, RAYON);
        super("Mine", DAMAGE, 1);
    }
    }

}
