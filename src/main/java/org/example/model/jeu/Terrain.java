package org.example.model.jeu;

import org.example.model.entites.Position;

import java.awt.image.BufferedImage;

import static org.example.model.jeu.TerrainGenerator.TILE_SIZE;

public class Terrain {
    private static final int WATER_LEVEL = 7;

    private final boolean[][] collisions;
    private final BufferedImage image;

    public Terrain(boolean[][] collisions, BufferedImage image) {
        this.collisions = collisions;
        this.image = image;
    }

    public void makeHole(int centerX, int centerY, int radius) {
        for (int y = centerX - radius; y <= centerX + radius; y++) {
            for (int x = centerY - radius; x <= centerY + radius; x++) {

                if (y >= 0 && y < getWidth() && x >= 0 && x < getHeight()) {
                    image.setRGB(x, y, 0x000000);

                    int colX = x / TILE_SIZE;
                    int colY = y / TILE_SIZE;
                    collisions[colY][colX] = false;
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
