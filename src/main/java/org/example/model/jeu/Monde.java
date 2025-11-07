package org.example.model.jeu;

import org.example.model.entites.Position;

public class Monde {
    private final int largeur;
    private final int hauteur;

    public Monde(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
    }


    public boolean positionValide(Position pos) {
        return pos.x >= 0 && pos.x < largeur && pos.y < hauteur;
    }


    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }
}
