package org.example.view.gui;

import java.util.HashMap;

public class AnimationManager {

    private static HashMap<String, Animation> animations = new HashMap<>();
    private static HashMap<String, Animation> zlerps = new HashMap<>();
    private static HashMap<String, Animation> glorbs = new HashMap<>();


    public AnimationManager() {
        // Animations joueur //
        zlerps.put("idle", new Animation("characters/zlerp/idle", 1));
        glorbs.put("idle", new Animation("characters/glorb/idle", 1));

        // Animations monde //
        animations.put("water", new Animation("animation/water", 8));
        //animations.put("explosion", new Animation("explosion", 4));

    }

    public static Animation getAnimation(String name) {
        return animations.get(name);
    }

    public static Animation getZlerp(String name) {
        return zlerps.get(name);
    }

    public static Animation getGlorb(String name) {
        return glorbs.get(name);
    }
}
