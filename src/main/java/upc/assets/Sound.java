package upc.assets;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {

    private Clip clip;
    private URL soundURL;

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
        if (clip == null) {
            return;
        }
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void setVolume(float volume) {
        if (clip != null) {
            FloatControl volumeControl = (FloatControl)
                    clip.getControl(FloatControl.Type.MASTER_GAIN);

            if (volumeControl != null) {
                volumeControl.setValue(volume);
            }
        }
    }
}
