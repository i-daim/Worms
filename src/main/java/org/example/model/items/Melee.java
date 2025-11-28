package org.example.model.items;

import org.example.model.entites.Alien;
import org.example.model.jeu.GameWorld;

public abstract class Melee extends Weapon implements Usable {
    //chaque arme de corps à corps provoque le déplacement d'un ver touché
    private int deplacement;

    public Melee(String nom, int degats, int rayon, int deplacement) {
        super(nom, degats, rayon);
        this.deplacement = deplacement;
    }

    @Override
    public void utiliser(GameWorld gameWorld, Alien alienActif) {
    }
}
