package upc.assets;


import java.util.HashMap;

public class SoundManager {

    private static final HashMap<String, Sound> MUSICS = new HashMap<>();
    private static final HashMap<String, Sound> EFFECTS = new HashMap<>();
    private static Sound[] voices;
    private static Sound currentMusic;

    static {
        loadVoices(11);

        MUSICS.put("Menu Theme", loadMusic("menu_theme"));
        MUSICS.put("Game Theme", loadMusic("game_theme"));
        MUSICS.put("Industry Glorby", loadMusic("industry_glorby"));
        MUSICS.put("Evanglerbion", loadMusic("evanglerbion"));
        MUSICS.put("Free Glirb", loadMusic("free_glirb"));
        MUSICS.put("What Is Glorb", loadMusic("what_is_glorb"));
        MUSICS.put("Five Glorbs At Zippy", loadMusic("five_glorbs_at_zippy"));
        MUSICS.put("Victory Music", loadMusic("victory_music"));
        MUSICS.put("Zip The Zoop Of Us", loadMusic("zip_the_zoop_of_us"));
        MUSICS.put("Caramellzlerpen", loadMusic("caramellzlerpen"));

        EFFECTS.put("Damage", loadEffect("damage"));
        EFFECTS.put("Explosion", loadEffect("explosion"));
        EFFECTS.put("Bazooka", loadEffect("bazooka"));
        EFFECTS.put("Rocket", loadEffect("rocket"));
        EFFECTS.put("Shotgun", loadEffect("shotgun"));
        EFFECTS.put("Uzi", loadEffect("uzi"));
        EFFECTS.put("Mine", loadEffect("mine_deploy"));
        EFFECTS.put("Impact", loadEffect("Impact"));
        EFFECTS.put("Grenade", loadEffect("grenade"));
        EFFECTS.put("Splash", loadEffect("water"));
    }


////    CONTROLES POUR LA MUSIQUE    ////
    public static void playMusic(String name) {
        if (isAudioDisabled()) {
            return;
        }
        if (currentMusic != null) {
            currentMusic.stop();
        }
        currentMusic = getMusic(name);
        if (currentMusic != null) {
            currentMusic.play();
            currentMusic.loop();
            currentMusic.setVolume(-10f);
        }
    }

    public static void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public static void playEffect(String name) {
        if (isAudioDisabled()) {
            return;
        }
        if (EFFECTS.get(name) != null) {
            Sound effect = EFFECTS.get(name).duplicate();
            effect.setVolume(-10f);
            effect.play();
        }
    }

    public static void playVoice() {
        int index = (int) (Math.random() * 11);
        playVoice(index);
    }
    public static void playVoice(int index) {
        if (isAudioDisabled()) {
            return;
        }
        if (index >= 0 && index < voices.length && voices[index] != null) {
            voices[index].play();
            voices[index].setVolume(-5f);
        }
    }


////    LOADERS    ////
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


////    GETTERS    ////
    public static Sound getMusic(String name) {
        return MUSICS.get(name);
    }

    public static Sound getCurrentMusic() {
        return currentMusic;
    }

    public static String[] getMusicsSelection() {
        return MUSICS.keySet().toArray(new String[0]);
    }

    private static boolean isAudioDisabled() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit")) {
                return true;
            }
        }
        return false;
    }
}
