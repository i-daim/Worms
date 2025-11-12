package org.example.model.items;

public class PiqueVer extends ArmeAFeu {
    public static final int DEGATS = 20;
    public static final int RAYON = 10;
    public static final int NBTIR = 5;

    public  PiqueVer(String nom) {
        super(nom, DEGATS, RAYON, NBTIR);
    }
}
