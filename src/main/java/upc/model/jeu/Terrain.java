package upc.model.jeu;

import upc.model.entites.Position;

import java.awt.image.BufferedImage;

import static upc.model.jeu.TerrainGenerator.TILE_SIZE;

/**
 * <p>Représente le terrain de jeu, y compris ses propriétés physiques et graphiques.</p>
 *
 * <p>Cette classe contient les données de collision ({@code boolean[][]}), l'image du terrain
 * destructible ({@code BufferedImage}), et l'image de fond. Elle fournit des méthodes
 * pour interagir avec le terrain, notamment pour y créer des trous et vérifier les collisions.</p>
 */
public class Terrain {
    /** Niveau vertical (en tuiles) où l'eau est considérée comme présente. */
    public static final int WATER_LEVEL = 170;

    /** Le tableau de booléens définissant la présence de collision pour chaque tuile. */
    private final boolean[][] collisions;
    /** L'image du terrain qui peut être modifiée (pour la destruction). */
    private final BufferedImage image;
    /** L'image de fond qui n'est pas affectée par la destruction. */
    private final BufferedImage background;

    /**
     * Constructeur pour {@code Terrain}.
     *
     * @param collisions Le tableau de collisions ({@code true} = solide, {@code false} = vide).
     * @param image L'image du terrain (premier plan, destructible).
     * @param background L'image de fond (non destructible).
     */
    public Terrain(boolean[][] collisions, BufferedImage image, BufferedImage background) {
        this.collisions = collisions;
        this.image = image;
        this.background = background;
    }

    /**
     * Crée un trou circulaire dans le terrain, simulant une explosion.
     * <p>
     * Cette méthode modifie l'image du terrain en rendant les pixels du cercle transparents
     * et met à jour le tableau de collisions en marquant les tuiles affectées comme {@code false}.
     * </p>
     *
     * @param centerX La coordonnée X du centre du trou (en pixels).
     * @param centerY La coordonnée Y du centre du trou (en pixels).
     * @param radius Le rayon du trou (en pixels).
     */
    public void makeHole(int centerX, int centerY, int radius) {
        // Boucle optimisée qui ne parcourt que le carré englobant le cercle
        for (int y = centerY - radius; y <= centerY + radius; y++) {
            for (int x = centerX - radius; x <= centerX + radius; x++) {

                // 1. Vérifie que les coordonnées sont dans les limites de l'image
                if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {

                    // 2. Vérifie que le pixel est dans le rayon (pour avoir un cercle)
                    // Utilise le théorème de Pythagore : distance = sqrt(dx^2 + dy^2)
                    double distance = Math.sqrt(Math.pow(x - centerX, 2)
                            + Math.pow(y - centerY, 2));
                    if (distance <= radius) {
                        // Rend le pixel transparent (canal alpha à 0)
                        image.setRGB(x, y, 0x00000000);

                        // 3. Mise à jour du tableau de collisions
                        int colX = x / TILE_SIZE;
                        int colY = y / TILE_SIZE;
                        if (colX >= 0 && colX < getCollisionsWidth()
                                && colY >= 0 && colY < getCollisionsHeight()) {
                            // Marque la tuile comme non solide (vide)
                            collisions[colY][colX] = false;
                        }
                    }
                }
            }
        }
    }


    /**
     * Retourne la largeur de l'image du terrain en pixels.
     *
     * @return La largeur en pixels.
     */
    public int getWidth() {
        return image.getWidth();
    }

    /**
     * Retourne la hauteur de l'image du terrain en pixels.
     *
     * @return La hauteur en pixels.
     */
    public int getHeight() {
        return image.getHeight();
    }

    /**
     * Retourne la largeur du tableau de collisions (en nombre de tuiles).
     *
     * @return Le nombre de colonnes dans le tableau de collisions.
     */
    public int getCollisionsWidth() {
        return collisions[0].length;
    }

    /**
     * Retourne la hauteur du tableau de collisions (en nombre de tuiles).
     *
     * @return Le nombre de lignes dans le tableau de collisions.
     */
    public int getCollisionsHeight() {
        return collisions.length;
    }

    /**
     * Retourne le tableau de collisions du terrain.
     *
     * @return Le tableau {@code boolean[][]}.
     */
    public boolean[][] getCollisions() {
        return collisions;
    }

    /**
     * Retourne l'image du terrain (premier plan, potentiellement destructible).
     *
     * @return Le {@code BufferedImage} du terrain.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Retourne l'image de fond du terrain.
     *
     * @return Le {@code BufferedImage} de l'arrière-plan.
     */
    public BufferedImage getBackground() {
        return background;
    }

    /**
     * Retourne le niveau de l'eau (en tuiles).
     *
     * @return La constante {@code WATER_LEVEL}.
     */
    public static int getWaterLevel() {
        return WATER_LEVEL;
    }

    /**
     * Vérifie s'il y a une collision à la coordonnée de tuile spécifiée.
     * <p>
     * Note: Toute coordonnée en dehors des limites du tableau est considérée comme une collision ({@code true}).
     * </p>
     *
     * @param x La coordonnée X de la tuile.
     * @param y La coordonnée Y de la tuile.
     * @return {@code true} s'il y a collision (ou hors limites), {@code false} sinon.
     */
    public boolean isCollision(int x, int y) {
        if (x < 0 || x >= getCollisionsWidth()
                || y < 0 || y >= getCollisionsHeight()) {
            return true;
        }
        return collisions[y][x];
    }

    public boolean isWater(int y) {
        return y >= WATER_LEVEL;
    }

    public boolean possibleMove(Position pos) {
        int posX = pos.getX();
        int posY = pos.getY();
        if (posX > getCollisionsWidth() ||  posX < 0
                || posY > getCollisionsHeight() || posY < 0) {
            return false;
        }
        return !collisions[posY][posX];
    }
}
