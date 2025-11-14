package org.example.model.jeu;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TerrainGenerator {

    private Gson gson = new Gson();

    private static class SimpleMap {
        private List<MapLayer> layers;
        private List<Tileset> tilesets;
        private int width;
        private int height;
    }

    private static class MapLayer {
        private List<Integer> data;
    }

    private static class Tileset {
        private String image;
        private int tilewidth;
        private int tileheight;
        private int columns;
    }

    public Terrain chargerTerrain(String fileName) {
        String filePath = "maps/" + fileName;

        var is = getClass().getClassLoader().getResourceAsStream(filePath);

        if (is == null) {
            throw new RuntimeException("Fichier non trouvé : " + filePath);
        }

        try (var reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            var mapData = gson.fromJson(reader, SimpleMap.class);

            if (mapData.tilesets == null || mapData.layers == null
            || mapData.tilesets.isEmpty() || mapData.layers.isEmpty()) {
                throw new RuntimeException("Map JSON invalide");
            }
            Tileset tileset = mapData.tilesets.get(0);
            MapLayer layer = mapData.layers.get(0);

            int largeur = mapData.width;
            int hauteur = mapData.height;
            List<Integer> data1D = layer.data;

            int[][] terrainType = new int[hauteur][largeur];
            for (int y = 0; y < hauteur; y++) {
                for (int x = 0; x < largeur; x++) {
                    int index1D = y * largeur + x;
                    int tileId = data1D.get(index1D);
                    terrainType[y][x] = tileId;
                }
            }
            return new Terrain(largeur, hauteur, terrainType,
                    tileset.tilewidth, tileset.tileheight, tileset.image, 1);

        } catch (Exception e) {
            System.err.println("Erreur en chargeant la map");
            e.printStackTrace();
            return null;
        }
    }
}
