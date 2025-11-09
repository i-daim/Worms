package org.example.model.entites;

import org.example.model.jeu.Equipe;
import org.example.model.jeu.Terrain;

public class Worm extends Entite {
    // Attributs
    private final Equipe equipe;
    private int pointsDeVie;
    private boolean vivant;
    private boolean actif;

    // Champs statiques
    private static final int DEPLACEMENT_DROITE = 5;
    private static final int DEPLACEMENT_GAUCHE = -5;
    private static final int SAUT = -20;


    // Constructeur
    public Worm(int pointsDeVie, Equipe equipe, int positionX, int positionY) {
        super(positionX, positionY);
        this.pointsDeVie = pointsDeVie;
        this.equipe = equipe;
        this.vivant = pointsDeVie > 0;
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

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean estActif() {
        return actif;
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

    public boolean deplacerDroite(Monde monde) {
        Position nouvellePos = getPosition().deplacer(DEPLACEMENT_DROITE, 0);
        return deplacer(nouvellePos, monde);
    }

    public boolean deplacerGauche(Monde monde) {
        Position nouvellePos = getPosition().deplacer(DEPLACEMENT_GAUCHE, 0);
        return deplacer(nouvellePos, monde);
    }

    public boolean sauter(Monde monde) {
        Position nouvellePos = getPosition().deplacer(0, SAUT);
        return deplacer(nouvellePos, monde);
    }

    private boolean positionValide(Position position, Terrain terrain) {
        int x = position.getX();
        int y = position.getY();

        // Vérifier que la position est dans les limites du terrain
        if (x < 0 || x >= terrain.getLargeur() || y < 0 || y >= terrain.getHauteur()) {
            return false;
        }

        // Vérifier qu'il n'y a pas de collision à cette position
        return !terrain.isCollision(x, y);
    }
}
