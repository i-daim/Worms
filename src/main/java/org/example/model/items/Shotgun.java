package org.example.model.items;

public class Shotgun extends Firearm {
    public static final int DEGATS = 25;
    public static final int RAYON = 5;
    public static final int NBTIR = 2;

    public Shotgun(String nom) {
        super(nom, DEGATS, RAYON, NBTIR);
    }

    //Implémenter plus tard une méthode tir surchager pour les armes de types fusils à pompes
    // par rapport à la réduction de la distance en lien avec la distance
}
