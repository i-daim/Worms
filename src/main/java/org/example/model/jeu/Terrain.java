package org.example.model.jeu;

import org.example.model.entites.Position;

import java.awt.image.BufferedImage;

public class Terrain {
    private static final int WATER_LEVEL = 7;

    private final int width;
    private final int height;
    private final boolean[][] collisions;
    private final BufferedImage image;

    public Terrain(int width, int height, boolean[][] collisions,  BufferedImage image) {
        this.width = width;
        this.height = height;
        this.collisions = collisions;
        this.image = image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
        if (x < 0 || x >= width || y < 0 || y >= height) {
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
        if (posX > width ||  posX < 0 || posY > height || posY < 0) {
            return false;
        }
        return !collisions[posY][posX];
    }
}
