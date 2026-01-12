package upc.assets;

import upc.model.entites.projectiles.Projectile;
import upc.model.entites.projectiles.Rocket;
import upc.model.items.Grenade;
import upc.model.items.Mine;
import upc.model.items.Shotgun;
import upc.model.items.Uzi;
import upc.model.items.Weapon;

import java.util.HashMap;

public class AnimationManager {

    private static HashMap<String, Animation> animations = new HashMap<>();
    private static HashMap<String, Animation> zlerps = new HashMap<>();
    private static HashMap<String, Animation> glorbs = new HashMap<>();

    static {
        // Animations aliens //
        // --- ZLERPS (Team 0) ---
        zlerps.put("idle_right",
                new Animation("characters/zlerp/idle", 1, false));
        zlerps.put("idle_left",
                new Animation("characters/zlerp/idle", 1, false));
        zlerps.put("walk_right",
                new Animation("characters/zlerp/walk", 5, false));
        zlerps.put("walk_left",
                new Animation("characters/zlerp/walk", 5, true));
        zlerps.put("jump_right",
                new Animation("characters/zlerp/jump", 2, false));
        zlerps.put("jump_left",
                new Animation("characters/zlerp/jump", 2, true));
        zlerps.put("air_right",
                new Animation("characters/zlerp/air", 2, false));
        zlerps.put("air_left",
                new Animation("characters/zlerp/air", 2, true));
        zlerps.put("land_right",
                new Animation("characters/zlerp/land", 2, false));
        zlerps.put("land_left",
                new Animation("characters/zlerp/land", 2, true));
        zlerps.put("fly_right",
                new Animation("characters/zlerp/fly", 3, false));
        zlerps.put("fly_left",
                new Animation("characters/zlerp/fly", 3, true));


        // --- GLORBS (Team 1) ---
        glorbs.put("idle_right",
                new Animation("characters/glorb/idle", 1, false));
        glorbs.put("idle_left",
                new Animation("characters/glorb/idle", 1, true));
        glorbs.put("walk_right",
                new Animation("characters/glorb/walk", 5, false));
        glorbs.put("walk_left",
                new Animation("characters/glorb/walk", 5, true));
        glorbs.put("jump_right",
                new Animation("characters/glorb/jump", 2, false));
        glorbs.put("jump_left",
                new Animation("characters/glorb/jump", 2, true));
        glorbs.put("air_right",
                new Animation("characters/glorb/air", 2, false));
        glorbs.put("air_left",
                new Animation("characters/glorb/air", 2, true));
        glorbs.put("land_right",
                new Animation("characters/glorb/land", 2, false));
        glorbs.put("land_left",
                new Animation("characters/glorb/land", 2, true));
        glorbs.put("fly_right",
                new Animation("characters/glorb/fly", 3, false));
        glorbs.put("fly_left",
                new Animation("characters/glorb/fly", 3, true));


        // Animations monde //
        animations.put("water",
                new Animation("animations/water/water", 8, false));
        animations.put("water_poison",
                new Animation("animations/poison/poison", 8, false));
        animations.put("water_hell",
                new Animation("animations/lava/lava", 8, false));

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
        Weapon weapon = p.getWeapon();

        if (p instanceof Rocket) {
            return getAnimation("rocket");
        } else if (weapon instanceof Uzi) {
            return getAnimation("bullet");
        } else if (weapon instanceof Shotgun) {
            return getAnimation("mini_bullet");
        } else if (weapon instanceof Grenade) {
            return getAnimation("grenade");
        } else if (weapon instanceof Mine) {
            return getAnimation("mine");
        }
        return getAnimation("bullet");
    }
}
