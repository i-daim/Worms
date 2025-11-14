package org.example.model.jeu;

public class Terrain {
    private static final int LIGNE_EAU = 25;

    private final int largeur;
    private final int hauteur;
    private final boolean[][] collisions;
    private final int[][] terrainType;

    private final int tileWidth;
    private final int tileHeight;
    private final String fileName;
    private final int tilesetColumns;


    public Terrain(int largeur, int hauteur, int[][] terrainType,
                   int tileWidth, int tileHeight, String fileName, int tilesetColumns) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.terrainType = terrainType;
        this.collisions = setCollisions(terrainType);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.fileName = fileName;
        this.tilesetColumns = tilesetColumns;
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

    public static int getLigneEau() {
        return LIGNE_EAU;
    }

    public boolean[][] setCollisions(int[][] terrainType) {
        boolean[][] collisions = new boolean[hauteur][largeur];
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                if (terrainType[y][x] != 0) {
                    collisions[y][x] = true;
                }
            }
        }
        return  collisions;
    }

    public boolean isCollision(int x, int y) {
        if (x < 0 || x >= largeur || y < 0 || y >= hauteur) {
            return true;
        }
        return collisions[y][x];
    }

    public boolean estDansEau(int y) {
        return y >= LIGNE_EAU;
    }

    public static Terrain creerTerrainVide(int largeur, int hauteur) {
        boolean[][] collisions = new boolean[hauteur][largeur];
        int[][] terrainType = new int[hauteur][largeur];
        return new Terrain(largeur, hauteur, terrainType, 0, 0, "", 0);
    }
}
