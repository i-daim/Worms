package upc.model.items;

import upc.model.entites.Alien;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameWorld;

public class Wings extends Tool {

    /**
     * Constructeur pour {@code Wings}
     */
    public Wings() {
        super("Wings");
    }

    @Override
    public Projectile useItem(GameWorld gameWorld, Alien alienActif) {
        return null;
    }

    /**
     * Retourne le type d'item correspondant à l'outil
     * @return l'objet {@link Type} représentant l'item en lien avec l'outil
     */
    @Override
    public Type getType() {
        return Type.WINGS;
    }

}