package org.example.model.items;

import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Shotgun extends Arme implements Utilisable {
    public static final int DEGATS = 20;
    public static final int RAYON = 5;

    public Shotgun(String nom) {
        super(nom,DEGATS,RAYON);
    }

    @Override
    public void utiliser(Monde monde, Worm wormActif) {
    }
}
