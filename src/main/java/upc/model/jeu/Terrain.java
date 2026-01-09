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

    private final String mapName;
    /** Le tableau de booléens définissant la présence de collision pour chaque tuile. */
    private final boolean[][] collisions;
    /** L'image du terrain qui peut être modifiée (pour la destruction). */
    private transient BufferedImage image;
    /** L'image de fond qui n'est pas affectée par la destruction. */
    private transient BufferedImage background;

    /**
     * Constructeur pour {@code Terrain}.
     *
     * @param mapName Le nom de la map.
     * @param collisions Le tableau de collisions ({@code true} = solide, {@code false} = vide).
     * @param image L'image du terrain (premier plan, destructible).
     * @param background L'image de fond (non destructible).
     */
    public Terrain(String mapName, boolean[][] collisions,
                   BufferedImage image, BufferedImage background) {
        this.mapName = mapName;
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
                        if (colX < getCollisionsWidth() && colY < getCollisionsHeight()) {
                            // Marque la tuile comme non solide (vide)
                            collisions[colY][colX] = false;
                        }
                    }
                }
            }
        }
    }

    /**
     * Met à jour l'image du terrain en fonction du tableau de collisions.
     * <p>
     * Cette méthode est utilisée pour la sauvegarde, pour synchroniser
     * l'image de la map avec le tableau de collisions.
     * </p>
     */
    public void updateMapImage() {
        if (image == null) {
            return;
        }

        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();

        for (int y = 0; y < getCollisionsHeight(); y++) {
            for (int x = 0; x < getCollisionsWidth(); x++) {
                if (!collisions[y][x]) {
                    // La tuile est vide, on rend les pixels correspondants transparents
                    int startX = x * TILE_SIZE;
                    int startY = y * TILE_SIZE;

                    for (int py = startY; py < startY + TILE_SIZE && py < imgHeight; py++) {
                        for (int px = startX; px < startX + TILE_SIZE && px < imgWidth; px++) {
                            image.setRGB(px, py, 0x00000000);
                        }
                    }
                }
            }
        }
    }


    public String getMapName() {
        return mapName;
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


    public void setBackground(BufferedImage imageBackground) {
        this.background = imageBackground;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Vérifie s'il y a une collision à la coordonnée de tuile spécifiée.
     * <p>
     * Note: Toute coordonnée dehors des limites du tableau est considérée
     * comme collision ({@code true}).
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

    /**
     * Vérifie si la coordonnée verticale (en tuiles) est au niveau de l'eau ou en dessous.
     *
     * @param y La coordonnée verticale (en tuiles).
     * @return {@code true} si la position est dans l'eau ou sous l'eau.
     */
    public boolean isWater(int y) {
        return y >= WATER_LEVEL;
    }

    /**
     * Vérifie si le déplacement vers la position de tuile spécifiée est possible.
     * <p>
     * Un mouvement est possible si la position est dans les limites et si la tuile
     * correspondante n'est pas en collision (est vide).
     * </p>
     *
     * @param pos La position de la tuile à vérifier.
     * @return {@code true} si le mouvement est possible
     * (pas de collision, dans les limites), {@code false} sinon.
     */
    public boolean possibleMove(Position pos) {
        int posX = pos.getX();
        int posY = pos.getY();

        // 1. Vérifie si la position est hors limites
        if (posX > getCollisionsWidth() - 1 || posX < 0
                || posY > getCollisionsHeight() - 1 || posY < 0) {
            return false;
        }
        // 2. Vérifie s'il n'y a pas de collision à cette position
        return !collisions[posY][posX];
    }

    /**
     * Retourne la clé (le nom) de l'animation à utiliser pour l'eau
     * en fonction du nom de la map.
     * @return la clé de l'animation pour {@link upc.assets.AnimationManager}
     */
    public String getWaterAnimationKey() {
        if (mapName == null) {
            return "water";
        }

        String name = mapName.toLowerCase();

        if (name.contains("poison")) {
            return "water_poison";
        } else if (name.contains("ice")) {
            return "water_ice";
        } else if (name.contains("hell")) {
            return "water_hell";
        }

        return "water";
    }

}
