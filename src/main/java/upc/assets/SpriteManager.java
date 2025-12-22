package upc.assets;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.io.IOException;



public class SpriteManager {
    private static final Map<String, BufferedImage> SPRITES = new HashMap<>();

    static {
        SpriteManager.load("Bazooka", "/Items/Bazooka.png");
        SpriteManager.load("Uzi", "Items/Uzi.png");
        SpriteManager.load("Shotgun", "Items/Shotgun.png");
        SpriteManager.load("Grenade", "Items/Grenade.png");
        SpriteManager.load("Mine", "Items/Mine.png");

        SpriteManager.load("Bazooka_Inv", "Items/Bazooka_Inv.png");
        SpriteManager.load("Uzi_Inv", "Items/Uzi_Inv.png");
        SpriteManager.load("Shotgun_Inv", "Items/Shotgun_Inv.png");
        SpriteManager.load("Grenade_Inv", "Items/Grenade_Inv.png");
        SpriteManager.load("Mine_Inv", "Items/Mine_Inv.png");

        SpriteManager.load("Bullet", "Items/Bullet.png");
        SpriteManager.load("MiniBullet", "Items/MiniBullet.png");
    }

    public static void load(String name, String path) {
        try {
            var url = SpriteManager.class.getResource(path);
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
