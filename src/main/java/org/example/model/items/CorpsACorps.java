package org.example.model.items;

import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public abstract class CorpsACorps extends Arme implements Utilisable {
    //chaque arme de corps à corps provoque le déplacement d'un ver touché
    private int deplacement;

    public CorpsACorps(String nom, int degats, int rayon, int deplacement) {
        super(nom, degats, rayon);
        this.deplacement = deplacement;
    }

    @Override
    public void utiliser(Monde monde, Worm wormActif) {
    }
}
