package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import upc.model.entites.Alien;
import upc.model.entites.Projectile;
import upc.model.jeu.Terrain;

import java.util.Map;


public class PhysicsEngine {
    private final Terrain terrain;
    private final World<Body> world;

    private final AlienPhysics alienPhysics;
    private final ProjectilePhysics projectilePhysics;

    private Vector2 wind = new Vector2(0, 0); // Vent (x, y)
    private final double dt = 1.0 / 60.0;
    private final Vector2 gravity = new Vector2(0, 60.0);

    public PhysicsEngine(Terrain terrain) {
        this.terrain = terrain;
        this.world = new World<>();
        this.world.setGravity(gravity);

        this.alienPhysics = new AlienPhysics(this);
        this.projectilePhysics = new ProjectilePhysics(this);

        TerrainPhysics terrainPhysics = new TerrainPhysics();
        terrainPhysics.createTerrainBodies(world, terrain.getCollisions());
    }

    public void update() {
        world.update(dt);
        updateAliens();
        updateProjectiles();
    }

    public void updateAliens() {
        alienPhysics.updateAliens();
        alienPhysics.limitFallVelocities();
    }

    public void updateProjectiles() {
        projectilePhysics.updateProjectiles(terrain);
    }

    public void setWind(double windX, double windY) {
        this.wind = new Vector2(windX, windY);
    }

    public Vector2 getWind() {
        return wind;
    }


    private void limitFallVelocities() {
        alienPhysics.limitFallVelocities();
    }

    private boolean isOnGround(Body body) {
        Vector2 vel = body.getLinearVelocity();
        return Math.abs(vel.y) < 3.0;
    }


    public void addWorm(Alien alien) {
        alienPhysics.addAlienBody(alien);
    }

    public void removeWorm(Alien alien) {
        alienPhysics.removeAlien(alien);
    }

    public void jumpWorm(Alien alien) {
        alienPhysics.jumpAlien(alien);
    }


    public void moveWorm(Alien alien, double direction) {
        alienPhysics.moveAlien(alien, direction);
    }

    public World<Body> getWorld() {
        return world;
    }

    public Body getWormBody(Alien alien) {
        return alienPhysics.getAlienBody(alien);
    }

    public void addProjectile(Projectile projectile, int angleDegrees, int powerPercent) {
        projectilePhysics.addProjectile(projectile, angleDegrees, powerPercent);
    }


    private void explodeProjectile(Projectile proj) {
        projectilePhysics.explodeProjectile(proj);
    }

    public ProjectilePhysics getProjectilePhysics() {
        return projectilePhysics;
    }

    public Map<Alien, Body> getAlienBodies() {
        return alienPhysics.getAlienBodies();
    }
}
