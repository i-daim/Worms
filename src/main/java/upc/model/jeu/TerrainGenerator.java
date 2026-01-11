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
     * Taille en pixels d'une tuile de collision dans l'image source.
     */
    public static final int TILE_SIZE = 4;

    private static final String[] MAPS = {"liberty", "spaceship", "hell", "poison", "ice"};

    public static Terrain chooseRandomMap() {
        int index = (int) (Math.random() * MAPS.length);
        String randomMap =  MAPS[index];
        return createTerrain(randomMap);
    }

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
     * @param mapName Le nom de la carte à charger (ex: "map_1").
     * Si {@code null} ou vide, un terrain vide est créé.
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

            // La taille du tableau de collisions (en nombre de tuiles)
            int collisionWidth = terrainImage.getWidth() / TILE_SIZE;
            int collisionHeight = terrainImage.getHeight() / TILE_SIZE;

            boolean[][] collisions =
                    new boolean[collisionHeight][collisionWidth];

            int imageWidth = terrainImage.getWidth();
            int imageHeight = terrainImage.getHeight();

            // Génération du tableau de collisions
            for (int y = 0; y < collisionHeight; y++) {
                for (int x = 0; x < collisionWidth; x++) {
                    // Calcule les coordonnées du pixel au centre du tile (le point de référence)
                    int centerX = x * TILE_SIZE + TILE_SIZE / 2;
                    int centerY = y * TILE_SIZE + TILE_SIZE / 2;

                    if (centerX >= imageWidth || centerY >= imageHeight) {
                        // S'assurer de ne pas dépasser les bords de l'image
                        collisions[y][x] = false;
                    } else {
                        // Récupère la valeur RGB du pixel central
                        int pixel = terrainImage.getRGB(centerX, centerY);
                        // Vérifie si le canal alpha est supérieur à zéro (non transparent)
                        // collisions[y][x] est TRUE si le pixel est OPAQUE.
                        collisions[y][x] = (0xff & (pixel >> 24)) > 0;
                    }
                }
            }

            // Tentative de chargement de l'image de fond
            try (InputStream backgroundIs = TerrainGenerator.class.getClassLoader()
                    .getResourceAsStream(backgroundPath)) {
                BufferedImage backgroundImage = ImageIO.read(backgroundIs);
                return new Terrain(mapName, collisions, terrainImage, backgroundImage);
            } catch (Exception e) {
                // Si le fond n'est pas trouvé, terrain créé sans fond, mais collisions valides
                System.err.println(
                        "Erreur (chargement du fond de la map) : " + e.getMessage());
                return new Terrain(mapName, collisions, terrainImage, null);
            }

        } catch (Exception e) {
            // Erreur critique (l'image du terrain principal est manquante ou illisible)
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

        int imgWidth = largeur * TILE_SIZE;
        int imgHeight = hauteur * TILE_SIZE;
        BufferedImage blankImage = new BufferedImage(imgWidth,
                imgHeight, BufferedImage.TYPE_INT_ARGB);

        return new Terrain("", collisions, blankImage, null);
    }
}
