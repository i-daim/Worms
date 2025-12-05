package upc.model.items;

import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;

public abstract class Melee extends Weapon implements Item {

    private int deplacement;

    private static final int NB_OF_HITS = 1;

    public Melee(String nom, int degats, int rayon, int deplacement) {
        super(nom, degats, NB_OF_HITS);
        this.deplacement = deplacement;
    }

    @Override
    public void useItem(GameWorld gameWorld, Alien alienActif) {
    }
}
