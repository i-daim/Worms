package upc.assets;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.io.IOException;



public class SpriteManager {
    private static final Map<String, BufferedImage> sprites = new HashMap<>();

    public static void load(String name, String path) {
        try {
            var url = SpriteManager.class.getResource(path);
            if (url != null) {
                sprites.put(name, ImageIO.read(url));
            } else {
                System.err.println("Sprite introuvable : " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage get(String name) {
        return sprites.get(name);
    }
}
