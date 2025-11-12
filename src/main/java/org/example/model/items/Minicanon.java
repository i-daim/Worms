package org.example.model.items;

public class Minicanon extends Arme_a_feu {
    public static final int DEGATS = 5;
    public static final int RAYON = 10;
    public static final int NBTIR = 50;

    public Minicanon(String nom){
        super(nom, DEGATS, RAYON, NBTIR);
    }

}
