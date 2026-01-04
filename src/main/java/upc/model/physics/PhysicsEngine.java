package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Terrain;
import org.dyn4j.world.listener.ContactListenerAdapter;
import org.dyn4j.world.ContactCollisionData;
import org.dyn4j.dynamics.contact.Contact;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class PhysicsEngine {

    private final GameWorld gameWorld;
    private final Terrain terrain;
    private final World<Body> world;

    private final TerrainPhysics terrainPhysics;
    private final AlienPhysics alienPhysics;
    private final ProjectilePhysics projectilePhysics;

    private Vector2 wind = new Vector2(0, 0);
    private final double dt = 1.0 / 60.0;
    private final Vector2 gravity = new Vector2(0, 60.0);

    private final Random random = new Random();
    private static final double MAX_WIND_POWER = 25.0;

    ////    CONSTRUCTEUR    ////
    public PhysicsEngine(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.terrain = gameWorld.getTerrain();
        this.world = new World<>();
        this.world.setGravity(gravity);

        this.world.addContactListener(new ContactListenerAdapter<Body>() {
            @Override
            public void begin(ContactCollisionData<Body> collision, Contact contact) {
                Body body1 = collision.getBody1();
                Body body2 = collision.getBody2();
                checkProjectileCollision(body1, body2);
                checkProjectileCollision(body2, body1);
            }
        });

        this.alienPhysics = new AlienPhysics(world, terrain);
        this.projectilePhysics = new ProjectilePhysics(this);
        this.terrainPhysics = new TerrainPhysics(world, terrain);

        terrainPhysics.rebuildTerrain();
    }


    ////    UPDATE DES PHYSIQUES    ////
    public void update() {
        world.update(dt);
        updateAliens();
        updateProjectiles();
        updateTerrain();
    }

    public void updateAliens() {
        alienPhysics.updateAliens();
        alienPhysics.limitFallVelocities();
    }

    public void updateProjectiles() {
        projectilePhysics.updateProjectiles(terrain);
    }

    public void updateTerrain() {
        terrainPhysics.updateTerrain();
    }


    ////    FONCTIONS POUR ALIENS    ////
    public void addAlien(Alien alien) {
        alienPhysics.addAlienBody(alien);
    }

    ////    FONCTIONS POUR PROJECTILES    ////
    public void addProjectile(Projectile projectile, int angleDegrees, int powerPercent) {
        projectilePhysics.addProjectile(projectile, angleDegrees, powerPercent);
    }

    private void checkProjectileCollision(Body b1, Body b2) {
        projectilePhysics.checkProjectileCollision(b1, b2);
    }


    public void randomizeWind() {
        // Calcul du facteur entre 0.25 et 1.0
        double powerFactor = 0.25 + (random.nextDouble() * 0.75);
        double direction = random.nextBoolean() ? 1.0 : -1.0;

        double windX = MAX_WIND_POWER * powerFactor * direction;
        double windY = (random.nextDouble() * 2.0) - 1.0;

        Vector2 windVector = new Vector2(windX, windY);

        // Application directe à la physique des projectiles
        this.projectilePhysics.setWind(windVector);

        // Met à jour la variable locale aussi si besoin pour affichage
        this.wind = windVector;
    }

    ////    GETTERS & SETTERS    ////
    public World<Body> getWorld() {
        return world;
    }

    public Map<Alien, Body> getAlienBodies() {
        return alienPhysics.getAlienBodies();
    }

    public List<Projectile> getActiveProjectiles() {
        return projectilePhysics.getActiveProjectiles();
    }

    public Body getAlienBody(Alien alien) {
        return alienPhysics.getAlienBody(alien);
    }

    public Vector2 getWind() {
        return wind;
    }

    public void setWind(double windX, double windY) {
        Vector2 v = new Vector2(windX, windY);
        this.wind = v;
        this.projectilePhysics.setWind(v);
    }

    public TerrainPhysics getTerrainPhysics() {
        return terrainPhysics;
    }

    public AlienPhysics getAlienPhysics() {
        return alienPhysics;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public ProjectilePhysics getProjectilePhysics() {
        return projectilePhysics;
    }
}