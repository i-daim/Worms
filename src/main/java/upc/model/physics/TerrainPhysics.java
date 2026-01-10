package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.world.World;
import upc.model.jeu.Terrain;
import upc.model.jeu.TerrainGenerator;


/**
 * <b>TerrainPhysics</b>
 * <p>
 * Cette classe gère la représentation physique du niveau (le sol, les murs).
 * Elle transforme la grille logique du jeu (Tableau 2D de booléens) en objets physiques solides
 * sur lesquels les Aliens peuvent marcher.
 * </p>
 * <p>
 * <b>Optimisation importante :</b>
 * Pour ne pas surcharger le moteur physique, on ne crée pas un carré par bloc.
 * On fusionne les blocs adjacents horizontalement pour créer de longs rectangles.
 * Cela réduit drastiquement le nombre de formes à gérer par et optimise énormement le code.
 * </p>
 */
public class TerrainPhysics {

    private final World<Body> world;
    private Terrain terrain;

    private Body terrainBody;

    // Flag pour savoir si le terrain a changé (ex: explosion) et doit être recréé
    private boolean modified = false;


    public TerrainPhysics(World<Body> world, Terrain terrain) {
        this.world = world;
        this.terrain = terrain;

        // Construction initiale du sol
        rebuildTerrain();
    }

    /**
     * Vérifie à chaque frame si le terrain a été modifié (creusé par une explosion).
     * Si oui, on reconstruit la physique.
     */
    public void updateTerrain() {
        if (modified) {
            rebuildTerrain();
            modified = false;
        }
    }

    /**
     * Met à jour le terrain en supprimant l'ancien corps et le remplaçant
     * par un nouveau avec les collisions actuelles
     */
    public void rebuildTerrain() {
        if (terrainBody != null) {
            world.removeBody(terrainBody);
        }

        terrainBody = new Body();
        // INFINITE = Objet statique, qui ne bouge jamais (masse infinie)
        terrainBody.setMass(MassType.INFINITE);

        // Génération des formes
        createTerrainBodies();

        // On verifie si le body n'est pas vide avant de l'ajouter
        if (terrainBody.getFixtureCount() > 0) {
            world.addBody(terrainBody);
        }
    }

    /**
     * Crée le corps du terrain en ajoutant des fixtures horizontales par ligne.
     */
    public void createTerrainBodies() {
        boolean[][] collisions = terrain.getCollisions();
        int height = collisions.length;
        int width = collisions[0].length;

        // Pour chaque ligne de la carte (Y)
        for (int y = 0; y < height; y++) {
            int startX = -1;

            // On parcourt les colonnes (X) + 1
            for (int x = 0; x <= width; x++) {
                // Est-ce un bloc solide
                boolean isCollision = (x < width) && collisions[y][x];

                if (isCollision) {
                    // Début d'un nouveau segment solide
                    if (startX == -1) {
                        startX = x;
                    }
                } else {
                    // Fin d'un segment solide
                    if (startX != -1) {
                        createRectangleFixture(startX, y, x - startX);
                        startX = -1;
                    }
                }
            }
        }
    }

    /**
     * Crée une fixture rectangle pour une ligne de collisions
     * @param tileX Position X de la fixture à créer
     * @param tileY Position Y de la fixture à créer
     * @param tileWidth Largeur de la fixture à créer
     */
    private void createRectangleFixture(int tileX, int tileY, int tileWidth) {
        double width = Converter.toMeters(tileWidth * TerrainGenerator.TILE_SIZE);
        double height = Converter.toMeters(TerrainGenerator.TILE_SIZE);

        // Calcul du centre du rectangle
        // Position coin gauche + (Largeur / 2)
        double x = Converter.toMeters((int)
                ((tileX * TerrainGenerator.TILE_SIZE) + (width * Converter.SCALE / 2)));
        double y = Converter.toMeters((int)
                ((tileY * TerrainGenerator.TILE_SIZE) + (height * Converter.SCALE / 2)));

        // Création de la forme géométrique
        BodyFixture fixture = new BodyFixture(
                Geometry.createRectangle(width, height));

        // Propriétés physiques du sol
        fixture.setFriction(0.8); // Rugueux pour ne pas glisser
        fixture.setRestitution(0.0); // Pas de rebond

        // On décale la forme relative au centre du Body (0,0) vers sa position réelle dans le monde
        fixture.getShape().translate(x, y);

        terrainBody.addFixture(fixture);
    }


    // --- Setters ---

    /**
     * Marque le terrain comme modifié.
     * Sera reconstruis au prochain appel de updateTerrain().
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * Change le terrain actif (chargement de niveau).
     */
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
        setModified(true);
    }
}
