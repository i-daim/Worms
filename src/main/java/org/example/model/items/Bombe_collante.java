package org.example.model.items;

import org.example.model.entites.Worm;

public class Bombe_collante extends Grenades_projectiles {
    public static final int DEGATS = 25;
    public static final int RAYON = 5;

    public Bombe_collante(String nom) {
        super(nom,DEGATS,RAYON);
    }

    @Override
    public void lancer(Worm wormActif){}
}
