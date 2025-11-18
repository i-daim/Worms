package org.example.model.jeu;

import java.awt.image.BufferedImage;

public class TerrainGenerator {

    private final static int TILE_SIZE = 16;

    public static Terrain creerTerrain(BufferedImage terrainImage) {
        // Taille du tableau de booleans
        int collisionWidth = terrainImage.getWidth() / TILE_SIZE;
        int collisionHeight = terrainImage.getHeight() / TILE_SIZE;

        boolean[][] collisions = new boolean[collisionHeight][collisionWidth];

        int imageWidth = terrainImage.getWidth();
        int imageHeight = terrainImage.getHeight();

        for (int y = 0; y < collisionHeight; y++) {
            for (int x = 0; x < collisionWidth; x++) {
                // Calcule les coordonnées du pixel au centre du tile
                int centerX = x * TILE_SIZE + TILE_SIZE / 2;
                int centerY = y * TILE_SIZE + TILE_SIZE / 2;

                if (centerX >= imageWidth || centerY >= imageHeight) {
                    collisions[y][x] = false;
                } else {
                    // Pixel : couleur du pixel aux coordonnées (centerX,centerY)
                    int pixel =  terrainImage.getRGB(centerX, centerY);
                    // Vérifie si le pixel est transparent : False si il l'est
                    collisions[y][x] = (0xff & (pixel >> 24)) > 0;
                }
            }
        }
        return new Terrain(imageWidth, imageHeight, collisions);
    }
}
