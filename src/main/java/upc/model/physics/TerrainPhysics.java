package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.world.World;
import upc.model.jeu.Converter;
import upc.model.jeu.TerrainGenerator;

public class TerrainPhysics {

    public void createTerrainBodies(World<Body> world, boolean[][] collisions) {
        int height = collisions.length;
        int width = collisions[0].length;

        for (int y = 0; y < height; y++) {
            int startX = -1;
            for (int x = 0; x <= width; x++) {
                boolean isCollision = (x < width) && collisions[y][x];

                if (isCollision && startX == -1) {
                    startX = x;
                } else if (!isCollision && startX != -1) {
                    createTerrainSegment(world, startX, y, x - startX, 1);
                    startX = -1;
                }
            }
        }
    }

    private void createTerrainSegment(World<Body> world, int tileX, int tileY, int tileWidth, int tileHeight) {
        Body body = new Body();

        double pixelX = tileX * TerrainGenerator.TILE_SIZE;
        double pixelY = tileY * TerrainGenerator.TILE_SIZE;
        double pixelWidth = tileWidth * TerrainGenerator.TILE_SIZE;
        double pixelHeight = tileHeight * TerrainGenerator.TILE_SIZE;

        double pixelCenterX = pixelX + (pixelWidth / 2.0);
        double pixelCenterY = pixelY + (pixelHeight / 2.0);

        double meterCenterX = Converter.toMeters((int) pixelCenterX);
        double meterCenterY = Converter.toMeters((int) pixelCenterY);
        double meterWidth = Converter.toMeters((int) pixelWidth);
        double meterHeight = Converter.toMeters((int) pixelHeight);

        BodyFixture fixture = new BodyFixture(
                Geometry.createRectangle(meterWidth, meterHeight));
        fixture.setFriction(0.8);
        fixture.setRestitution(0.1);

        body.addFixture(fixture);
        body.translate(meterCenterX, meterCenterY);
        body.setMass(MassType.INFINITE);

        world.addBody(body);
    }

}
