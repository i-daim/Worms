package org.example.model.items;

public class Shotgun extends ArmeAFeu {
    public static final int DEGATS = 25;
    public static final int RAYON = 5;
    public static final int NBTIR = 2;

    public Shotgun(String nom) {
        super(nom, DEGATS, RAYON, NBTIR);
    }

}
