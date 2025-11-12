package org.example.model.items;


public class BombeCollante extends GrenadeProjectile {
    public static final int DEGATS = 25;
    public static final int RAYON = 5;

    public BombeCollante(String nom) {
        super(nom, DEGATS, RAYON);
    }
}
