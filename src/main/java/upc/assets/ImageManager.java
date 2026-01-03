package upc.assets;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.io.IOException;



public class ImageManager {
    private static final Map<String, BufferedImage> IMAGES = new HashMap<>();

    static {
        ImageManager.load("menu_background", "/menu/menu_background.png");
        ImageManager.load("settings_background", "/menu/settings_background.png");

        ImageManager.load("Bazooka", "/Items/Bazooka.png");
        ImageManager.load("Uzi", "/Items/Uzi.png");
        ImageManager.load("Shotgun", "/Items/Shotgun.png");
        ImageManager.load("Grenade", "/Items/Grenade.png");
        ImageManager.load("Mine", "/Items/Mine.png");

        ImageManager.load("Bazooka_Inv", "/Items/Bazooka_Inv.png");
        ImageManager.load("Uzi_Inv", "/Items/Uzi_Inv.png");
        ImageManager.load("Shotgun_Inv", "/Items/Shotgun_Inv.png");
        ImageManager.load("Grenade_Inv", "/Items/Grenade_Inv.png");
        ImageManager.load("Mine_Inv", "/Items/Mine_Inv.png");
        ImageManager.load("Jetpack", "/Items/Jetpack0.png");
    }

    public static void load(String name, String path) {
        try {
            var url = ImageManager.class.getResource(path);
            if (url != null) {
                IMAGES.put(name, ImageIO.read(url));
            } else {
                System.err.println("Sprite introuvable : " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage get(String name) {
        return IMAGES.get(name);
    }
}
