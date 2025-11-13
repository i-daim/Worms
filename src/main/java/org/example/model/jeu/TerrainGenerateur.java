package org.example.model.jeu;

public class TerrainGenerateur {

    public static Terrain creerMapSimple() {
        int largeur = 50;
        int hauteur = 30;

        boolean[][] collisions = new boolean[hauteur][largeur];
        int[][] terrainType = new int[hauteur][largeur];

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                if (y < 15) {
                    terrainType[y][x] = 0;
                    collisions[y][x] = false;
                } else if (y < Terrain.getLigneEau()) {
                    terrainType[y][x] = 2;
                    collisions[y][x] = true;
                } else {
                    terrainType[y][x] = 0;
                    collisions[y][x] = false;
                }
            }
        }

        for (int x = 10; x < 20; x++) {
            terrainType[15][x] = 1;
            collisions[15][x] = true;
        }

        return new Terrain(largeur, hauteur, collisions, terrainType);
    }
}
