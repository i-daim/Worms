package org.example.model.jeu;

import org.example.model.entites.Position;

public class Terrain {
    private static final int WATER_LEVEL = 7;

    private final int width;
    private final int height;
    private final boolean[][] collisions;
    private final int[][] terrainType;

    private final int tileWidth;
    private final int tileHeight;
    private final String fileName;
    private final int tilesetColumns;


    public Terrain(int width, int height, int[][] terrainType,
                   int tileWidth, int tileHeight, String fileName, int tilesetColumns) {
        this.width = width;
        this.height = height;
        this.terrainType = terrainType;
        this.collisions = setCollisions(terrainType);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.fileName = fileName;
        this.tilesetColumns = tilesetColumns;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static int getWaterLevel() {
        return WATER_LEVEL;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public String getFileName() {
        return fileName;
    }

    public int getTilesetColumns() {
        return tilesetColumns;
    }

    public int getTileId(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return 0;
        } else {
            return terrainType[y][x];
        }
    }

    public boolean[][] setCollisions(int[][] terrainType) {
        boolean[][] collisions = new boolean[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (terrainType[y][x] != 0) {
                    collisions[y][x] = true;
                }
            }
        }
        return  collisions;
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
