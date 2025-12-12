package upc.assets;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

public class Animation {
    // Attributs : nom, et liste d'images
    private final String name;
    private final ArrayList<BufferedImage> frames;

    // Constructeur
    public Animation(String name, int numFrames, boolean flip) {
        this.name = name;
        this.frames = new ArrayList<>(numFrames);

        for (int i = 0; i < numFrames; i++) {
            frames.add(loadFrame(i, flip));
        }
    }

    /**
     * Méthode qui récupère une frame de l'animation. <br>
     * Cherche une image avec le nom + l'index fourni (par ex : walk0)
     * en ajoutant les champs requis.
     * @param index : L'index de l'image, fourni par la boucle qui appelle la méthode
     * @param flip : Permet d'inverser l'image pour les animations des aliens
     * @return
     */
    private BufferedImage loadFrame(int index, boolean flip) {
        String path = "/" + name + index + ".png";

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Fichier d'image non trouvé : " + path);
            }

            BufferedImage image = ImageIO.read(is);

            // Option pour inverser l'image (animations gauche/droite)
            if (flip) {
                int width = image.getWidth();
                int height = image.getHeight();
                BufferedImage flipped = new BufferedImage(width, height, image.getType());
                Graphics2D g = flipped.createGraphics();

                g.drawImage(image, width, 0, -width, height, null);
                g.dispose();

                return flipped;
            }

            return image;

        } catch (Exception e) {
            throw new RuntimeException("Erreur (chargement de l'animation " + name + ")", e);
        }
    }

    /**
     * Renvoie la liste de frames de l'animation.
      * @return : ArrayList de BufferedImages
     */
    public ArrayList<BufferedImage> getFrames() {
        return frames;
    }
}
