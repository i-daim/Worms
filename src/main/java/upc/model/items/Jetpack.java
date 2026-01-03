package upc.model.items;

import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;

public class Jetpack extends Tool {

    public Jetpack() {
        super("Jetpack");
    }

    @Override
    public void useItem(GameWorld gameWorld, Alien alienActif) {
        // Le jetpack ne tire pas, l'action sera gérée via méthode fly()

    }

    @Override
    public Type getType() {
        return Type.JETPACK;
    }

}