package org.example.model.items;

public class Mine_antichar extends Pieges_explosifs{
    public static final int DEGATS = 60;
    public static final int RAYON = 3;
    public Mine_antichar(String nom){
        super(nom, DEGATS, RAYON);
    }
}
