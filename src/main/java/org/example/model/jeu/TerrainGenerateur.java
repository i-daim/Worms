package org.example.model.jeu;

public class TerrainGenerateur {

    public static Terrain creerMapSimple() {
        int largeur = 30;
        int hauteur = 15;

        boolean[][] collisions = new boolean[hauteur][largeur];
        int[][] terrainType = new int[hauteur][largeur];

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                if (y >= 10) {
                    terrainType[y][x] = 2;
                    collisions[y][x] = true;
                } else {
                    terrainType[y][x] = 0;
                    collisions[y][x] = false;
                }
            }
        }

        return new Terrain(largeur, hauteur, collisions, terrainType);
    }
}
