package upc.assets;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.io.IOException;



public class ImageManager {
    private static final Map<String, BufferedImage> SPRITES = new HashMap<>();

    static {
        SpriteManager.load("Bazooka", "/Items/Bazooka2.png");
    }

    public static void load(String name, String path) {
        try {
            var url = ImageManager.class.getResource(path);
            if (url != null) {
                SPRITES.put(name, ImageIO.read(url));
            } else {
                System.err.println("Sprite introuvable : " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage get(String name) {
        return SPRITES.get(name);
    }
}
