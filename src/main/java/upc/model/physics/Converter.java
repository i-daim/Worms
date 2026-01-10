package upc.model.physics;

/**
 * <b>Converter</b>
 * <p>
 * Cette classe utilitaire statique sert de passerelle entre le monde du rendu (Pixels)
 * et le monde de la physique (Mètres).
 * </p>
 * <p>
 * <b>Pourquoi c'est nécessaire ?</b><br>
 * Le moteur physique (Dyn4j) simule la réalité et utilise le système métrique (MKS).
 * Si on lui envoyait des coordonnées en pixels (ex: 1920x1080), il penserait que
 * les objets font 1 km de large, et la simulation serait très lente et lourde ("floaty").
 * </p>
 * <p>
 * On utilise donc cette classe pour convertir les coordonnées à la volée.
 * </p>
 */
public class Converter {
    public static final double SCALE = 20.0;

    public static double toMeters(int pixels) {
        return pixels / SCALE;
    }

    public static int toPixels(double meters) {
        return (int) (meters * SCALE);
    }
}
