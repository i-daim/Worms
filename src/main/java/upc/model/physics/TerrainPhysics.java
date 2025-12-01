package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.world.World;
import upc.model.jeu.Terrain;
import upc.model.jeu.TerrainGenerator;

public class TerrainPhysics {

    private final World<Body> world;
    private final Terrain terrain;

    private Body terrainBody;

    public TerrainPhysics(World<Body> world, Terrain terrain) {
        this.world = world;
        this.terrain = terrain;

        updateTerrain();
    }

    /**
     * Met à jour le terrain en supprimant l'ancien corps et le remplaçant
     * par un nouveau avec les collisions actuelles
     */
    public void updateTerrain() {
        if (terrainBody != null) {
            world.removeBody(terrainBody);
        }

        terrainBody = new Body();
        terrainBody.setMass(MassType.INFINITE);

        createTerrainBodies();

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

        for (int y = 0; y < height; y++) {
            int startX = -1;

            for (int x = 0; x <= width; x++) {
                boolean isCollision = (x < width) && collisions[y][x];

                if (isCollision) {
                    if (startX == -1) {
                        startX = x;
                    }
                } else {
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
     */
    private void createRectangleFixture(int tileX, int tileY, int tileWidth) {
        double width = Converter.toMeters(tileWidth * TerrainGenerator.TILE_SIZE);
        double height = Converter.toMeters(TerrainGenerator.TILE_SIZE);

        double x = Converter.toMeters((int)
                ((tileX * TerrainGenerator.TILE_SIZE) + (width * Converter.SCALE / 2)));
        double y = Converter.toMeters((int)
                ((tileY * TerrainGenerator.TILE_SIZE) + (height * Converter.SCALE / 2)));

        BodyFixture fixture = new BodyFixture(
                Geometry.createRectangle(width, height));
        fixture.setFriction(0.8);
        fixture.setRestitution(0.0);

        fixture.getShape().translate(x, y);

        terrainBody.addFixture(fixture);
    }
}
