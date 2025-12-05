package upc.view.gui;

import java.util.HashMap;

public class AnimationManager {

    private static HashMap<String, Animation> animations = new HashMap<>();
    private static HashMap<String, Animation> zlerps = new HashMap<>();
    private static HashMap<String, Animation> glorbs = new HashMap<>();


    public AnimationManager() {
        // Animations joueur //
        zlerps.put("idle", new Animation("characters/zlerp/idle", 1, false));
        zlerps.put("walk_right", new Animation("characters/zlerp/walk", 6, false));
        zlerps.put("walk_left", new Animation("characters/zlerp/walk", 6, true));
        zlerps.put("jump", new Animation("characters/zlerp/jump", 4, false));

        glorbs.put("idle", new Animation("characters/glorb/idle", 1, false));

        // Animations monde //
        animations.put("water", new Animation("animations/water/water", 8, false));
        //animations.put("explosion", new Animation("explosion", 4));

    }

    public static Animation getAnimation(String name) {
        return animations.get(name);
    }

    public static Animation getSprite(int type, String name) {
        if (type == 0) {
            if (!zlerps.containsKey(name)) {
                return zlerps.get("idle");
            }
            return zlerps.get(name);
        } else {
            if (!glorbs.containsKey(name)) {
                return glorbs.get("idle");
            }
            return glorbs.get(name);
        }
    }
}
