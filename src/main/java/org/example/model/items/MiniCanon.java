package org.example.model.items;

public class MiniCanon extends ArmeAFeu {
    public static final int DEGATS = 5;
    public static final int RAYON = 10;
    public static final int NBTIR = 50;

    public MiniCanon(String nom){
        super(nom, DEGATS, RAYON, NBTIR);
    }

}
