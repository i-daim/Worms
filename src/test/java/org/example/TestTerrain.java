package org.example;
import upc.model.jeu.Terrain;
import upc.model.entites.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static upc.model.jeu.TerrainGenerator.*;


import java.awt.image.BufferedImage;
import java.awt.Color;

// Simuler TILE_SIZE qui vient de TerrainGenerator dans le code original// En pratique, vous devriez importer la vraie constante TILE_SIZE.
class TerrainGenerator {
    public static final int TILE_SIZE = 10; // Valeur arbitraire pour le test
}

public class TestTerrain {

    // Dimensions pour les tests
    private static final int COL_WIDTH = 10;
    private static final int COL_HEIGHT = 5;
    private static final int IMG_WIDTH = COL_WIDTH * TILE_SIZE; // 100
    private static final int IMG_HEIGHT = COL_HEIGHT * TILE_SIZE; // 50

    // Outils de test réutilisables
    private boolean[][] createCollisionArray(int colWidth, int colHeight) {
        return new boolean[colHeight][colWidth];
    }

    private BufferedImage createImage(int width, int height) {
        // Crée une image simple, par exemple complètement blanche (opaque)
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    // Le setup des tests
    private Terrain setupTerrain(boolean[][] collisions) {
        BufferedImage image = createImage(IMG_WIDTH, IMG_HEIGHT);
        BufferedImage background = createImage(IMG_WIDTH, IMG_HEIGHT);
        return new Terrain(collisions, image, background);
    }

    // --- Tests mis à jour ---

    @Test
    public void testConstructeurEtGettersBase() {
        boolean[][] collisions = createCollisionArray(COL_WIDTH, COL_HEIGHT);
        BufferedImage image = createImage(IMG_WIDTH, IMG_HEIGHT);
        BufferedImage background = createImage(IMG_WIDTH, IMG_HEIGHT);

        Terrain t = new Terrain(collisions, image, background);

        // Test des Getters
        assertEquals(IMG_WIDTH, t.getWidth(), "La largeur d'image doit être correcte.");
        assertEquals(IMG_HEIGHT, t.getHeight(), "La hauteur d'image doit être correcte.");
        assertEquals(COL_WIDTH, t.getCollisionsWidth(), "La largeur de la matrice de collisions doit être correcte.");
        assertEquals(COL_HEIGHT, t.getCollisionsHeight(), "La hauteur de la matrice de collisions doit être correcte.");
        assertEquals(collisions, t.getCollisions(), "La référence de la matrice de collisions doit être correcte.");
        assertEquals(image, t.getImage(), "La référence de l'image doit être correcte.");
        assertEquals(background, t.getBackground(), "La référence de l'arrière-plan doit être correcte.");
    }

    @Test
    public void testIsCollision() {
        boolean[][] collisions = createCollisionArray(COL_WIDTH, COL_HEIGHT);
        collisions[2][3] = true; // Collision à (colX=3, colY=2)
        Terrain terrain = setupTerrain(collisions);

        // Test collision à l'intérieur
        assertTrue(terrain.isCollision(3, 2), "Doit détecter une collision aux coordonnées (3, 2).");
        assertFalse(terrain.isCollision(4, 4), "Ne doit pas détecter de collision aux coordonnées (4, 4).");

        // Test hors limites (doit retourner true)
        assertTrue(terrain.isCollision(COL_WIDTH, 0), "Doit retourner true pour les coordonnées X hors limites.");
        assertTrue(terrain.isCollision(0, COL_HEIGHT), "Doit retourner true pour les coordonnées Y hors limites.");
        assertTrue(terrain.isCollision(-1, 0), "Doit retourner true pour les coordonnées négatives.");
    }

    @Test
    public void testPossibleMove() {
        boolean[][] collisions = createCollisionArray(COL_WIDTH, COL_HEIGHT);
        collisions[1][1] = true; // Collision à (colX=1, colY=1)
        Terrain terrain = setupTerrain(collisions);

        // Mouvement possible (pas de collision)
        assertTrue(terrain.possibleMove(new Position(0, 0)), "Le mouvement à (0, 0) devrait être possible.");

        // Mouvement impossible (collision)
        assertFalse(terrain.possibleMove(new Position(1, 1)), "Le mouvement à (1, 1) ne devrait pas être possible (collision).");

        // Mouvement hors limites
        assertFalse(terrain.possibleMove(new Position(COL_WIDTH , 0)), "Le mouvement aux limites maximales (X) devrait être impossible.");
        assertFalse(terrain.possibleMove(new Position(0, COL_HEIGHT)), "Le mouvement aux limites maximales (Y) devrait être impossible.");
        assertFalse(terrain.possibleMove(new Position(-1, 0)), "Le mouvement hors limites (X négatif) devrait être impossible.");
    }

    @Test
    public void testIsWater() {
        // WATER_LEVEL est défini à 7
        assertEquals(7, Terrain.getWaterLevel(), "La méthode statique getWaterLevel doit retourner 7.");

        Terrain terrain = setupTerrain(createCollisionArray(COL_WIDTH, COL_HEIGHT)); // L'objet terrain n'est pas strictement nécessaire

        assertTrue(terrain.isWater(7), "y >= WATER_LEVEL (7) doit être true.");
        assertTrue(terrain.isWater(10), "y > WATER_LEVEL doit être true.");
        assertFalse(terrain.isWater(6), "y < WATER_LEVEL doit être false.");
    }

    @Test
    public void testMakeHole() {
        boolean[][] collisions = createCollisionArray(COL_WIDTH, COL_HEIGHT);
        BufferedImage image = createImage(IMG_WIDTH, IMG_HEIGHT);
        // Initialiser l'image avec une couleur opaque (ex: blanc) pour vérifier la transparence
        for (int y = 0; y < IMG_HEIGHT; y++) {
            for (int x = 0; x < IMG_WIDTH; x++) {
                image.setRGB(x, y, Color.WHITE.getRGB());
            }
        }
        Terrain terrain = new Terrain(collisions, image, createImage(IMG_WIDTH, IMG_HEIGHT));

        // Placer une collision pour vérifier sa suppression
        int colX = 1;
        int colY = 1;
        collisions[colY][colX] = true; // Collision à la position de la tuile (1, 1)

        // Centre du trou en coordonnées PIXEL (au centre de la tuile (1, 1))
        int centerX = (colX * TILE_SIZE) + (TILE_SIZE / 2); // 1*10 + 5 = 15
        int centerY = (colY * TILE_SIZE) + (TILE_SIZE / 2); // 1*10 + 5 = 15
        int radius = 5; // Un rayon qui couvre la tuile (1, 1)

        terrain.makeHole(centerX, centerY, radius);

        // 1. Vérification de la transparence de l'image (Pixel au centre du trou)
        int centerPixel = image.getRGB(centerX, centerY);
        assertEquals(0x00000000, centerPixel, "Le pixel central (15, 15) doit être rendu transparent (0x00000000).");

        // 2. Vérification de la mise à jour des collisions
        assertFalse(collisions[colY][colX], "La collision à la tuile (1, 1) doit être désactivée (makeHole met collisions[colY][colX] à false).");

        // 3. Vérification qu'une tuile éloignée n'est pas affectée (Ex: tuile (0, 0))
        int outsideX = TILE_SIZE / 2; // Pixel au centre de la tuile (0, 0)
        int outsideY = TILE_SIZE / 2;
        assertNotEquals(0x00000000, image.getRGB(outsideX, outsideY), "Le pixel extérieur ne doit pas être transparent.");
    }

    // Les tests précédents comme testGetLargeur, testGetHauteur, etc. ne sont plus valides ou sont inclus dans testConstructeurEtGettersBase
    // La méthode 'creerTerrainVide' n'existe plus et n'est donc pas testée.
}