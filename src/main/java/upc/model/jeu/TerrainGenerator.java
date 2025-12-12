package upc.model.jeu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * <p>Classe utilitaire responsable de la génération et du chargement des maps {@link Terrain}.</p>
 *
 * <p>Elle fournit des méthodes statiques pour charger les données de collision à partir
 * d'images PNG et pour créer des terrains par défaut (vides).</p>
 */
public class TerrainGenerator {

    /**
     * Taille en pixels d'une tuile (tile) de collision dans l'image source.
     * <p>Une tuile de collision est de {@code TILE_SIZE} x {@code TILE_SIZE} pixels.</p>
     */
    public static final int TILE_SIZE = 4;


    /**
     * Crée un objet {@link Terrain} en chargeant les données de collision à partir
     * d'une image PNG. La collision est déterminée par la transparence des pixels.
     * <p>
     * Le processus de chargement :
     * <ul>
     * <li>Charge l'image du terrain ({@code maps/[mapName].png}).</li>
     * <li>Calcule un tableau de booléens ({@code collisions}) où chaque entrée
     * représente une tuile de {@code TILE_SIZE} x {@code TILE_SIZE} pixels.</li>
     * <li>Une tuile est considérée comme solide ({@code true}) si le pixel central
     * correspondant dans l'image n'est pas transparent.</li>
     * <li>Tente de charger l'image de fond ({@code maps/[mapName]_background.png}).</li>
     * </ul>
     * </p>
     *
     * @param mapName Le nom de la carte à charger (ex: "map_1"). Si {@code null} ou vide, un terrain vide est créé.
     * @return Un nouvel objet {@link Terrain} contenant les données de collision et les images,
     * ou {@code null} en cas d'échec de chargement critique de l'image principale.
     */
    public static Terrain createTerrain(String mapName) {
        if (mapName == null || mapName.isEmpty()) {
            return createEmptyTerrain(40, 10);
        }

        String filePath = "maps/" + mapName + ".png";
        String backgroundPath = "maps/" + mapName + "_background.png";

        try (InputStream is = TerrainGenerator.class.getClassLoader()
                .getResourceAsStream(filePath)) {
            if (is == null) {
                throw new RuntimeException(
                        "Fichier d'image non trouvé : " + filePath);
            }

            BufferedImage terrainImage = ImageIO.read(is);

            // Taille du tableau de booleans
            int collisionWidth = terrainImage.getWidth() / TILE_SIZE;
            int collisionHeight = terrainImage.getHeight() / TILE_SIZE;

            boolean[][] collisions =
                    new boolean[collisionHeight][collisionWidth];

            int imageWidth = terrainImage.getWidth();
            int imageHeight = terrainImage.getHeight();

            for (int y = 0; y < collisionHeight; y++) {
                for (int x = 0; x < collisionWidth; x++) {
                    // Calcule les coordonnées du pixel au centre du tile
                    int centerX = x * TILE_SIZE + TILE_SIZE / 2;
                    int centerY = y * TILE_SIZE + TILE_SIZE / 2;

                    if (centerX >= imageWidth || centerY >= imageHeight) {
                        collisions[y][x] = false;
                    } else {
                        // Pixel : couleur du pixel aux coordonnées (centerX,centerY)
                        int pixel = terrainImage.getRGB(centerX, centerY);
                        // Vérifie si le pixel est transparent : False si il l'est
                        collisions[y][x] = (0xff & (pixel >> 24)) > 0;
                    }
                }
            }

            try (InputStream backgroundIs = TerrainGenerator.class.getClassLoader()
                    .getResourceAsStream(backgroundPath)) {
                BufferedImage backgroundImage = ImageIO.read(backgroundIs);
                return new Terrain(collisions, terrainImage, backgroundImage);

            } catch (Exception e) {
                System.err.println(
                        "Erreur (chargement du fond de la map) : " + e.getMessage());
                return new Terrain(collisions, terrainImage, null);
            }

        } catch (Exception e) {
            System.err.println(
                    "Erreur (chargement de la map) : " + e.getMessage());
            return null;
        }
    }

    /**
     * Crée un objet {@link Terrain} simple avec des dimensions spécifiées et
     * génère une plateforme centrale de base pour le jeu.
     * <p>
     * Ce terrain est utilisé comme valeur par défaut ou de repli.
     * Il ne contient aucune image ({@code null}).
     * </p>
     *
     * @param largeur La largeur du tableau de collisions (en tuiles).
     * @param hauteur La hauteur du tableau de collisions (en tuiles).
     * @return Un nouvel objet {@link Terrain} avec une plateforme centrale.
     */
    public static Terrain createEmptyTerrain(int largeur, int hauteur) {
        boolean[][] collisions = new boolean[hauteur][largeur];

        int plateformeX = 3;
        int plateformeY = hauteur / 2 - 1;

        for (int y = plateformeY; y < plateformeY + 2; y++) {
            for (int x = plateformeX; x < largeur - plateformeX; x++) {
                collisions[y][x] = true;
            }
        }
        return new Terrain(collisions, null, null);
    }
}
