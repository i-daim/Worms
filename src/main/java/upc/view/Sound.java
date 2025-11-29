package upc.view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

    Clip clip;
    URL soundURL;

    public Sound(String path) {
        try {
            soundURL = getClass().getResource(path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();

    }

    public void stop() {
        clip.stop();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void playSound(String name) {
        new Sound("/sounds/" + name + ".wav").play();
    }
}
