package org.example.model.items;

public class Uzi extends Arme_a_feu{
    public static final int DEGATS = 2;
    public static final int RAYON = 10;
    public static final int NBTIR = 25;

    public Uzi(String nom) {
        super(nom,DEGATS,RAYON,NBTIR);
    }

}
