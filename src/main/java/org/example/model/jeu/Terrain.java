package org.example.model.jeu;

public class Terrain {

    private final int largeur;
    private final int hauteur;

    private final boolean[][] collisions;
    private final int[][] terrainType;

    public Terrain(int largeur, int hauteur, boolean[][] collisions, int[][] terrainType) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.collisions = collisions;
        this.terrainType = terrainType;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int[][] getTerrainType() {
        return terrainType;
    }

    public boolean isCollision(int x, int y) {
        return collisions[x][y];
    }
}
