package upc.assets;

import upc.model.entites.projectiles.Projectile;
import upc.model.entites.projectiles.Rocket;
import upc.model.entites.projectiles.ShotgunBullet;
import upc.model.entites.projectiles.Throwable;
import upc.model.entites.projectiles.Trap;
import upc.model.entites.projectiles.UziBullet;

import java.util.HashMap;

public class AnimationManager {

    private static HashMap<String, Animation> animations = new HashMap<>();
    private static HashMap<String, Animation> zlerps = new HashMap<>();
    private static HashMap<String, Animation> glorbs = new HashMap<>();

    static {
        // Animations aliens //
        zlerps.put("idle_right",
                new Animation("characters/zlerp/idle", 1, false));
        zlerps.put("idle_left",
                new Animation("characters/zlerp/idle", 1, false));
        zlerps.put("walk_right",
                new Animation("characters/zlerp/walk", 5, false));
        zlerps.put("walk_left",
                new Animation("characters/zlerp/walk", 5, true));

        glorbs.put("idle_right",
                new Animation("characters/glorb/idle", 1, false));
        glorbs.put("idle_left",
                new Animation("characters/glorb/idle", 1, true));
        glorbs.put("walk_right",
                new Animation("characters/glorb/walk", 5, false));
        glorbs.put("walk_left",
                new Animation("characters/glorb/walk", 5, true));


        // Animations monde //
        animations.put("water",
                new Animation("animations/water/water", 8, false));
        animations.put("explosion",
                new Animation("animations/explosion/explosion", 7, false));

        // Projectiles //
        animations.put("rocket",
                new Animation("animations/projectiles/rocket", 9, false));
        animations.put("bullet",
                new Animation("animations/projectiles/bullet", 1, false));
        animations.put("grenade",
                new Animation("animations/projectiles/grenade", 1, false));
        animations.put("mini_bullet",
                new Animation("animations/projectiles/mini_bullet", 1, false));
        animations.put("mine",
                new Animation("animations/projectiles/mine", 1, false));


        // Autres //
        animations.put("victory_glorbs",
                new Animation("animations/victory/glorb/victory_glorb", 45, false));
        animations.put("victory_zlerps",
                new Animation("animations/victory/zlerp/victory_zlerp", 45, false));
    }


////    GETTERS    ////
    public static Animation getAnimation(String name) {
        if (!animations.containsKey(name)) {
            throw new RuntimeException("Erreur lors du chargement de l'animation : " + name);
        }
        return animations.get(name);
    }

    public static Animation getSprite(int type, String name, boolean direction) {
        String fullName = name + (direction ? "_right" : "_left");

        if (type == 0) {
            if (!zlerps.containsKey(fullName)) {
                return zlerps.get("idle_right");
            }
            return zlerps.get(fullName);
        } else {
            if (!glorbs.containsKey(fullName)) {
                return glorbs.get("idle_right");
            }
            return glorbs.get(fullName);
        }
    }

    public static Animation getProjectile(Projectile p) {
        return switch (p) {
            case Rocket rocket -> animations.get("rocket");
            case UziBullet bullet -> animations.get("bullet");
            case ShotgunBullet shotgunBullet -> animations.get("mini_bullet");
            case Throwable grenade -> animations.get("grenade");
            case Trap mine -> animations.get("mine");
            default -> null;
        };
    }
}
