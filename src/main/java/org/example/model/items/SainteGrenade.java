package org.example.model.items;

public class SainteGrenade extends GrenadeProjectile {
    public static final int DEGATS = 65;
    public static final int RAYON = 10;

    public SainteGrenade(String nom) {
        super(nom, DEGATS, RAYON);
    }
}
