package org.example.model.entites;

import org.example.model.jeu.Equipe;
import org.example.model.jeu.Terrain;

public class Worm extends Entite {
    // Attributs
    private final Equipe equipe;
    private int pointsDeVie;
    private boolean vivant;

    // Champs statiques
    private static final int DEPLACEMENT_DROITE = 5;
    private static final int DEPLACEMENT_GAUCHE = -5;
    private static final int SAUT = -20;
    private static final int PV_INITIAUX = 100;


    // Constructeur
    public Worm(Equipe equipe, int positionX, int positionY) {
        super(positionX, positionY);
        this.pointsDeVie = PV_INITIAUX;
        this.equipe = equipe;
        this.vivant = true;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public boolean estVivant() {
        return vivant;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void subirDegats(int degats) {
        if (!vivant) {
            return;
        }

        if (degats < 0) {
            degats = 0;
        }

        pointsDeVie -= degats;

        if (pointsDeVie <= 0) {
            pointsDeVie = 0;
            vivant = false;
        }
    }

    public boolean deplacer(Position nouvellePosition, Terrain terrain) {
        if (!vivant) {
            return false;
        }

        if (positionValide(nouvellePosition, terrain)) {
            setPosition(nouvellePosition);
            return true;
        }

        return false;
    }

    public boolean deplacerDroite(Terrain terrain) {
        Position nouvellePos = getPosition().deplacer(DEPLACEMENT_DROITE, 0);
        return deplacer(nouvellePos, terrain);
    }

    public boolean deplacerGauche(Terrain terrain) {
        Position nouvellePos = getPosition().deplacer(DEPLACEMENT_GAUCHE, 0);
        return deplacer(nouvellePos, terrain);
    }


    public boolean sauter(Terrain terrain) {
        Position nouvellePos = getPosition().deplacer(0, SAUT);
        return deplacer(nouvellePos, terrain);
    }

    private boolean positionValide(Position position, Terrain terrain) {
        int x = position.getX();
        int y = position.getY();

        if (x < 0 || x >= terrain.getWidth() || y < 0 || y >= terrain.getHeight()) {
            return false;
        }


        return !terrain.isCollision(x, y);
    }
    @Override
    public String toString() {
        return "Worm[PV=" + pointsDeVie + ", Vivant=" + vivant + "]";
    }

}
