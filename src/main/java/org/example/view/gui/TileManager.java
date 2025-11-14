package org.example.view.gui;

import org.example.model.jeu.Terrain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;

public class TileManager {

    private final HashMap<Integer, BufferedImage> tilesMap = new HashMap<>();

    public TileManager(Terrain terrain) {
        String fileName = terrain.getFileName();
        int tileWidth = terrain.getTileWidth();
        int tileHeight = terrain.getTileHeight();
        int tilesetColumns = terrain.getTilesetColumns();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new RuntimeException("Fichier d'image non trouvé : " + fileName);
            }

            BufferedImage image = ImageIO.read(is);

            int id = 1;
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

    }
            for (int y = 0; y < imageHeight; y += tileHeight) {
                for (int x = 0; x < imageWidth; x += tileWidth) {
                    BufferedImage tile = image.getSubimage(x, y, tileWidth, tileHeight);
                    tilesMap.put(id, tile);
                    id++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
