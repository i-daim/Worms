package upc.assets;

import java.util.HashMap;

public class SoundManager {

    private static final HashMap<String, Sound> MUSICS = new HashMap<>();
    private static final HashMap<String, Sound> EFFECTS = new HashMap<>();
    private static Sound[] voices;

    static {
        loadVoices(11);

        MUSICS.put("Menu Theme", loadMusic("menu_theme"));
        MUSICS.put("Game Theme", loadMusic("game_theme"));
        MUSICS.put("Industry Glorby", loadMusic("industry_glorby"));
        MUSICS.put("Evanglerbion", loadMusic("evanglerbion"));
        MUSICS.put("Free Glirb", loadMusic("free_glirb"));
        MUSICS.put("What Is Glorb", loadMusic("what_is_glorb"));
        MUSICS.put("Five Glorbs At Zippy", loadMusic("five_glorbs_at_zippy"));

        EFFECTS.put("Damage", loadEffect("damage"));
        EFFECTS.put("Explosion", loadEffect("explosion"));
        EFFECTS.put("Bazooka", loadEffect("bazooka"));
        EFFECTS.put("Rocket", loadEffect("rocket"));
        EFFECTS.put("Shotgun", loadEffect("shotgun"));
        EFFECTS.put("Uzi", loadEffect("uzi"));
        EFFECTS.put("Mine", loadEffect("mine_deploy"));


    }

////    GETTERS POUR LES SONS    ////
    public static Sound getMusic(String name) {
        return MUSICS.get(name);
    }

    public static void playEffect(String name) {
        Sound effect = EFFECTS.get(name);
        if (effect != null) {
            effect.play();
        }
    }

    public static void playVoice() {
        int index = (int) (Math.random() * 11);
        voices[index].play();
    }
    public static void playVoice(int index) {
        if (index >= 0 && index < voices.length && voices[index] != null) {
            voices[index].play();
        }
    }

////    METHODES POUR CHARGER LES SONS    ////
    public static void loadVoices(int number) {
        voices = new Sound[number];
        for (int i = 0; i < number; i++) {
            voices[i] = new Sound("/sounds/voices/voice" + i + ".wav");
        }
    }

    public static Sound loadMusic(String name) {
        return new Sound("/sounds/music/" + name + ".wav");
    }

    public static Sound loadEffect(String name) {
        return new Sound("/sounds/effects/" + name + ".wav");
    }
}
