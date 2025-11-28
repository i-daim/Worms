package org.example.model.items;

public class Mine extends Trap {
    public static final int DEGATS = 45;
    public static final int RAYON = 3;
    public Mine(String nom) {
        super(nom, DEGATS, RAYON);
    }

}
