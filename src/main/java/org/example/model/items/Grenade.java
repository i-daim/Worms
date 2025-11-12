package org.example.model.items;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Grenade extends GrenadeProjectile  {
    public static final int DEGATS = 45;
    public static final int RAYON = 5;

    public Grenade(String nom) {
        super(nom,DEGATS,RAYON);
    }

}
