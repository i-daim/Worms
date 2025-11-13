package org.example.model.jeu;

public class Terrain {
    private final int largeur;
    private final int hauteur;
    private final boolean[][] collisions;
    private final int[][] terrainType;
    private final int ligneEau;

    public Terrain(int largeur, int hauteur, boolean[][] collisions,
                   int[][] terrainType, int ligneEau) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.collisions = collisions;
        this.terrainType = terrainType;
        this.ligneEau = ligneEau;
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

    public int getLigneEau() {
        return ligneEau;
    }

    public boolean isCollision(int x, int y) {
        if (x < 0 || x >= largeur || y < 0 || y >= hauteur) {
            return true;
        }
        return collisions[y][x];
    }

    public boolean estDansEau(int y) {
        return y >= ligneEau;
    }

    public int getTileId(int x, int y) {
        if (x < 0 || x >= largeur || y < 0 || y >= hauteur) {
            return 0;
        }
        return terrainType[y][x];
    }

    public static Terrain creerTerrainVide(int largeur, int hauteur) {
        boolean[][] collisions = new boolean[hauteur][largeur];
        int[][] terrainType = new int[hauteur][largeur];
        return new Terrain(largeur, hauteur, collisions, terrainType, hauteur);
    }
}
