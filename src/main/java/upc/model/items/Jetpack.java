package upc.model.items;

import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;

public class Jetpack extends Weapon {

    public Jetpack() {
        super("Jetpack", 0, 1, 0);
    }

    @Override
    public void useItem(GameWorld gameWorld, Alien alienActif) {
        // Le jetpack ne tire pas, l'action sera gérée via méthode fly() (à finir)

    }
}