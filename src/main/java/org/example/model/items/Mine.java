package org.example.model.items;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Mine extends PiegeExplosif {
    public static final int DEGATS = 45;
    public static final int RAYON = 3;
    public Mine(String nom){ super(nom, DEGATS, RAYON);}

}
