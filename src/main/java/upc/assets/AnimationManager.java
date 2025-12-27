package upc.assets;

import java.util.HashMap;

public class AnimationManager {

    private static HashMap<String, Animation> animations = new HashMap<>();
    private static HashMap<String, Animation> zlerps = new HashMap<>();
    private static HashMap<String, Animation> glorbs = new HashMap<>();

    static {
        // Animations joueur //
        zlerps.put("idle_right", new Animation("characters/zlerp/idle", 1, false));
        zlerps.put("idle_left", new Animation("characters/zlerp/idle", 1, false));
        zlerps.put("walk_right", new Animation("characters/zlerp/walk", 5, false));
        zlerps.put("walk_left", new Animation("characters/zlerp/walk", 5, true));
//        zlerps.put("jump", new Animation("characters/zlerp/jump", 4, false));

        glorbs.put("idle_right", new Animation("characters/glorb/idle", 1, false));
        glorbs.put("idle_left", new Animation("characters/glorb/idle", 1, true));
        glorbs.put("walk_right", new Animation("characters/glorb/walk", 5, false));
        glorbs.put("walk_left", new Animation("characters/glorb/walk", 5, true));


        // Animations monde //
        animations.put("water", new Animation("animations/water/water", 8, false));
        animations.put("explosion", new Animation("animations/explosion/explosion", 7, false));
        animations.put("rocket", new Animation("animations/Rocket/Rocket", 9, false));

        // Autres //
        animations.put("victory_glorbs", new Animation("animations/victory/glorb/victory_glorb", 45, true));
        animations.put("victory_zlerps", new Animation("animations/victory/zlerp/victory_zlerp", 45, true));
    }

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
}
