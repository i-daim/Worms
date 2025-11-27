package org.example.view.gui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

public class Animation {
    private final String name;
    private final ArrayList<BufferedImage> frames;

    public Animation(String name, int numFrames) {
        this.name = name;
        this.frames = new ArrayList<>(numFrames);

        for (int i = 0; i < numFrames; i++) {
            frames.add(loadFrame(i));
        }
    }

    public ArrayList<BufferedImage> getFrames() {
        return frames;
    }

    public BufferedImage loadFrame(int index) {
        String path = "/" + name + index + ".png";

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Fichier d'image non trouvé : " + path);
            }
            return ImageIO.read(is);

        } catch (Exception e) {
            System.err.println("Erreur (chargement de l'animation " + name + ") : " + e.getMessage());
            return null;
        }
    }
}
