package upc.model.jeu;

import upc.model.entites.Position;

import java.awt.image.BufferedImage;

import static upc.model.jeu.TerrainGenerator.TILE_SIZE;

/**
 * <p>Représente le terrain de jeu, y compris ses propriétés physiques et graphiques.</p>
 *
 * <p>Cette classe contient les données de collision ({@code boolean[][]}), l'image du terrain
 * destructible ({@code BufferedImage}), et l'image de fond. Elle fournit des méthodes
 * pour interagir avec le terrain, notamment pour y créer des trous et vérifier les collisions.</p>
 */
public class Terrain {
    public static final int WATER_LEVEL = 170;

    private final boolean[][] collisions;
    private final BufferedImage image;
    private final BufferedImage background;

    public Terrain(boolean[][] collisions, BufferedImage image, BufferedImage background) {
        this.collisions = collisions;
        this.image = image;
        this.background = background;
    }

    public void makeHole(int centerX, int centerY, int radius) {
        // Boucle qui commence à la position du trou pour mieux réduire les calculs
        for (int y = centerY - radius; y <= centerY + radius; y++) {
            for (int x = centerX - radius; x <= centerX + radius; x++) {

                // Vérifie que les coordonnées sont dans l'image
                if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {

                    // Vérifie que le pixel est dans le rayon
                    // (pour avoir un cercle au lieu d'un carré)
                    double distance = Math.sqrt(Math.pow(x - centerX, 2)
                            + Math.pow(y - centerY, 2));
                    if (distance <= radius) {
                        image.setRGB(x, y, 0x00000000); // Rend le pixel transparent

                        // Màj des collisions (à vérifier)
                        int colX = x / TILE_SIZE;
                        int colY = y / TILE_SIZE;
                        if (colX >= 0 && colX < getCollisionsWidth()
                                && colY >= 0 && colY < getCollisionsHeight()) {
                            collisions[colY][colX] = false;
                        }
                    }
                }
            }
        }
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getCollisionsWidth() {
        return collisions[0].length;
    }

    public int getCollisionsHeight() {
        return collisions.length;
    }

    public boolean[][] getCollisions() {
        return collisions;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getBackground() {
        return background;
    }

    public static int getWaterLevel() {
        return WATER_LEVEL;
    }

    public boolean isCollision(int x, int y) {
        if (x < 0 || x >= getCollisionsWidth()
                || y < 0 || y >= getCollisionsHeight()) {
            return true;
        }
        return collisions[y][x];
    }

    public boolean isWater(int y) {
        return y >= WATER_LEVEL;
    }

    public boolean possibleMove(Position pos) {
        int posX = pos.getX();
        int posY = pos.getY();
        if (posX > getCollisionsWidth() ||  posX < 0
                || posY > getCollisionsHeight() || posY < 0) {
            return false;
        }
        return !collisions[posY][posX];
    }
}
