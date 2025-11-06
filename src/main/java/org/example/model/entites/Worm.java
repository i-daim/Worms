package org.example.model.entites;

import org.example.model.jeu.Equipe;
import org.example.model.jeu.Monde;

public class Worm extends Entite {
    private int pointsDeVie;
    private final Equipe equipe;
    private Position position;
    private boolean vivant;

    public Worm(int pointsDeVie, Equipe equipe, int positionX, int positionY) {
        super(positionX, positionY);
        this.pointsDeVie = pointsDeVie;
        this.equipe = equipe;
        this.position = new Position(positionX, positionY);
        this.vivant = pointsDeVie > 0;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public boolean estVivant() {
        return vivant;
    }

    public Position getPosition() {
        return position;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void subirDegats(int degats) {
        if (!vivant) return;

        if (degats < 0) {
            degats = 0;
        }

        pointsDeVie -= degats;

        if (pointsDeVie <= 0) {
            pointsDeVie = 0;
            vivant = false;
        }
    }

    public boolean deplacer(Position nouvellePosition, Monde monde) {
        if (!vivant) return false;

        if (monde.positionValide(nouvellePosition)) {
            this.position = nouvellePosition;
            return true;
        }

        return false;
    }

    public boolean deplacerDroite(Monde monde) {
        Position nouvellePos = position.deplacer(5, 0);
        return deplacer(nouvellePos, monde);
    }

    public boolean deplacerGauche(Monde monde) {
        Position nouvellePos = position.deplacer(-5, 0);
        return deplacer(nouvellePos, monde);
    }

    public boolean sauter(Monde monde) {
        Position nouvellePos = position.deplacer(0, -10);  // Saut plus réaliste
        return deplacer(nouvellePos, monde);
    }

}
