package upc.model.items;

import upc.model.entites.Alien;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameWorld;

public class Wings extends Tool {

    private static final int NB_OF_SHOTS = 10;

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

    @Override
    public int getNbOfShots() {
        return NB_OF_SHOTS;
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