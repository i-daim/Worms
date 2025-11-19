package org.example.model.jeu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class TerrainGenerator {

    private static final int TILE_SIZE = 16;


    public static Terrain creerTerrain(String mapName) {
        if (mapName == null || mapName.isEmpty()) {
            return creerTerrainVide(40, 10);
        }

        String filePath = "maps/" + mapName;

        try (InputStream is = TerrainGenerator.class.getClassLoader()
                .getResourceAsStream(filePath)) {
            if (is == null) {
                throw new RuntimeException(
                        "Fichier d'image non trouvé : " + filePath);
            }

            BufferedImage terrainImage = ImageIO.read(is);

            // Taille du tableau de booleans
            int collisionWidth = terrainImage.getWidth() / TILE_SIZE;
            int collisionHeight = terrainImage.getHeight() / TILE_SIZE;

            boolean[][] collisions =
                    new boolean[collisionHeight][collisionWidth];

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
                        int pixel = terrainImage.getRGB(centerX, centerY);
                        // Vérifie si le pixel est transparent : False si il l'est
                        collisions[y][x] = (0xff & (pixel >> 24)) > 0;
                    }
                }
            }
            return new Terrain(imageWidth, imageHeight, collisions, terrainImage);

        } catch (Exception e) {
            System.err.println(
                    "Erreur (chargement de la map) : " + e.getMessage());
            return null;
        }
    }

    public static Terrain creerTerrainVide(int largeur, int hauteur) {
        boolean[][] collisions = new boolean[hauteur][largeur];

        int plateformeX = 3;
        int plateformeY = hauteur / 2 - 1;

        for (int y = plateformeY; y < plateformeY + 2; y++) {
            for (int x = plateformeX; x < largeur - plateformeX; x++) {
                collisions[y][x] = true;
            }
        }
        return new Terrain(largeur, hauteur, collisions, null);
    }
}
