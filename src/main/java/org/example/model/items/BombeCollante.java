package org.example.model.items;

import org.example.model.entites.Worm;

public class BombeCollante extends GrenadeProjectile {
    public static final int DEGATS = 25;
    public static final int RAYON = 5;

    public BombeCollante(String nom) {
        super(nom,DEGATS,RAYON);
    }

    @Override
    public void lancer(Worm wormActif){}
}
