package upc.assets;

import java.util.HashMap;

public class SoundManager {

    private static final HashMap<String, Sound> MUSICS = new HashMap<>();
    private static final HashMap<String, Sound> EFFECTS = new HashMap<>();
    private static Sound[] voices;

    static {
        MUSICS.put("Menu Theme", loadMusic("menu_theme"));
        MUSICS.put("Game Theme", loadMusic("game_theme"));
        MUSICS.put("Industry Glorby", loadMusic("industry_glorby"));
        MUSICS.put("Evanglerbion", loadMusic("evanglerbion"));
        MUSICS.put("Free Glirb", loadMusic("free_glirb"));
        MUSICS.put("What Is Glorb", loadMusic("what_is_glorb"));
        MUSICS.put("Five Glorbs At Zippy", loadMusic("five_glorbs_at_zippy"));

    }


    public static Sound loadMusic(String name) {
        return new Sound("/sounds/music/" + name + ".wav");
    }

    public static Sound loadEffect(String name) {
        return new Sound("/sounds/effects/" + name + ".wav");
    }
}
