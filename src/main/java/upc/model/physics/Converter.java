package upc.model.physics;

/**
 * <b>Converter</b>
 * <p>
 * Cette classe utilitaire statique sert de passerelle entre le monde du rendu
 * et le monde de la physique.
 * </p>
 * <p>
 * <b>Utilité de la classe /b><br>
 * Le moteur physique (Dyn4j) simule la réalité et utilise le système métrique.
 * Si on lui envoyait des coordonnées en pixels (ex: 1920x1080), il penserait que
 * les objets font 1 km de large, et la simulation serait très lente.
 * </p>
 * <p>
 * On utilise donc cette classe pour convertir les coordonnées à la volée.
 * </p>
 */
public class Converter {

    /**
     * <b>Le Ratio Pixels par Mètre (PPM).</b>
     * <br>
     * Ici, 20 pixels à l'écran équivalent à 1 mètre dans la simulation physique.
     * <br>
     * <i>Exemple : Un personnage de 40px de haut fera 2 mètres physiques.</i>
     */
    public static final double SCALE = 20.0;

    /**
     * Convertis une distance ou une position de l'écran (Pixels) vers la physique (Mètres).
     * Utilisé lors de la création d'un objet ou pour gérer les clics souris.
     *
     * @param pixels : La valeur en pixels (int).
     * @return : La valeur convertie en mètres (double).
     */
    public static double toMeters(int pixels) {
        return pixels / SCALE;
    }

    /**
     * Convertit une position physique (Mètres) vers une position écran (Pixels).
     * Utilisé à chaque frame dans la boucle de rendu pour savoir où dessiner les sprites
     * en fonction de la position du Body physique.
     *
     * @param meters : La valeur en mètres (double), venant souvent de Dyn4j.
     * @return : La valeur en pixels (int), prête pour l'affichage.
     */
    public static int toPixels(double meters) {
        return (int) (meters * SCALE);
    }
}
