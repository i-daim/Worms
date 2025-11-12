package org.example.model.items;

public class Pique_ver extends Arme_a_feu{
    public static final int DEGATS = 20;
    public static final int RAYON = 10;
    public static final int NBTIR = 5;

    public  Pique_ver(String nom){
        super(nom, DEGATS, RAYON, NBTIR);
    }
}
