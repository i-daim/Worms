package org.example.model.jeu;

import java.awt.image.BufferedImage;

public class TerrainGenerator {

    public static boolean[][] creerCollisions(BufferedImage terrainImage, int tileSize) {
        // Taille du tableau de booleans
        int collisionWidth = terrainImage.getWidth() / tileSize;
        int collisionHeight = terrainImage.getHeight() / tileSize;

        boolean[][] collisions = new boolean[collisionHeight][collisionWidth];

        int imageWidth = terrainImage.getWidth();
        int imageHeight = terrainImage.getHeight();

        for (int y = 0; y < collisionHeight; y++) {
            for (int x = 0; x < collisionWidth; x++) {
                // Calcule les coordonnées du pixel au centre du tile
                int centerX = x * tileSize + tileSize / 2;
                int centerY = y * tileSize + tileSize / 2;

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
        return collisions;
    }
}
