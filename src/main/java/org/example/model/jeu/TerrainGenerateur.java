package org.example.model.jeu;

public class TerrainGenerateur {

    public static Terrain creerMapSimple() {
        int largeur = 50;
        int hauteur = 30;

        boolean[][] collisions = new boolean[hauteur][largeur];
        int[][] terrainType = new int[hauteur][largeur];

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                if (y < 10) {
                    terrainType[y][x] = 0;
                    collisions[y][x] = false;
                } else if (y == 10) {
                    terrainType[y][x] = 1;
                    collisions[y][x] = true;
                } else if (y < 20) {
                    terrainType[y][x] = 2;
                    collisions[y][x] = true;
                } else {
                    terrainType[y][x] = 0;
                    collisions[y][x] = false;
                }
            }
        }

        for (int x = 15; x < 25; x++) {
            terrainType[9][x] = 1;
            collisions[9][x] = true;
        }

        for (int x = 30; x < 40; x++) {
            terrainType[8][x] = 1;
            collisions[8][x] = true;
        }

        return new Terrain(largeur, hauteur, collisions, terrainType);
    }
}
