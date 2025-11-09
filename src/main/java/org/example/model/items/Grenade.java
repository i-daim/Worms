package org.example.model.items;

import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Grenade extends Arme implements Utilisable {
    public static final int DEGATS = 50;
    public static final int RAYON = 3;

    public Grenade(String nom) {
        super(nom,DEGATS,RAYON);
    }

    @Override
    public void utiliser(Monde monde, Worm wormActif) {
        int explosionX = wormActif.getPosition().getX();
        int explosionY = wormActif.getPosition().getY();

    }
}
