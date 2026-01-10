package upc.model.items;

import upc.model.entites.Alien;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameWorld;

public class Jetpack extends Tool {

    /**
     * Constructeur pour {@code Jetpack}
     */
    public Jetpack() {
        super("Jetpack");
    }

    @Override
    public Projectile useItem(GameWorld gameWorld, Alien alienActif) {
        // Le jetpack ne tire pas, l'action sera gérée via méthode fly()
        return null;
    }

    /**
     * Retourne le type d'item correspondant à l'outil
     * @return l'objet {@link Type} représentant l'item en lien avec l'outil
     */
    @Override
    public Type getType() {
        return Type.JETPACK;
    }

}