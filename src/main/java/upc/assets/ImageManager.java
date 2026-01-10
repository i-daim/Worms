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

        ImageManager.load("Bazooka", "/items/bazooka.png");
        ImageManager.load("Uzi", "/items/uzi.png");
        ImageManager.load("Shotgun", "/items/shotgun.png");
        ImageManager.load("Grenade", "/items/grenade.png");
        ImageManager.load("Mine", "/items/mine.png");
        ImageManager.load("jetpack", "/items/jetpack.png");

        ImageManager.load("Bazooka_Inv", "/items/bazooka_inv.png");
        ImageManager.load("Uzi_Inv", "/items/uzi_inv.png");
        ImageManager.load("Shotgun_Inv", "/items/shotgun_inv.png");
        ImageManager.load("Grenade_Inv", "/items/grenade_inv.png");
        ImageManager.load("Mine_Inv", "/items/mine_inv.png");
        ImageManager.load("Jetpack_Inv", "/items/jetpack_inv.png");
    }

    public static void load(String name, String path) {
        try {
            var url = ImageManager.class.getResource(path);
            if (url != null) {
                IMAGES.put(name, ImageIO.read(url));
            } else {
                System.err.println("Image introuvable : " + path);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image " + name + " : " + path);
            System.err.println(e.getMessage());
        }
    }

    public static BufferedImage get(String name) {
        return IMAGES.get(name);
    }
}
