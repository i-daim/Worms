package org.example.model.items;

public class Grenade extends Throwable {
    public static final int DEGATS = 45;
    public static final int RAYON = 5;

    public Grenade(String nom) {
        super(nom, DEGATS, RAYON);
    }

}
