package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Bullet;
import upc.model.entites.projectiles.Projectile;
import upc.model.entites.projectiles.Rocket;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Terrain;
import org.dyn4j.world.listener.ContactListenerAdapter;
import org.dyn4j.world.ContactCollisionData;
import org.dyn4j.dynamics.contact.Contact;

import java.util.List;
import java.util.Map;

public class PhysicsEngine {

    private final GameWorld gameWorld;
    private final Terrain terrain;
    private final World<Body> world;

    private final TerrainPhysics terrainPhysics;
    private final AlienPhysics alienPhysics;
    private final ProjectilePhysics projectilePhysics;

    private Vector2 wind = new Vector2(0, 0); // Vent (x, y)
    private final double dt = 1.0 / 60.0;
    private final Vector2 gravity = new Vector2(0, 60.0);

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
    private void checkProjectileCollision(Body b1, Body b2) {
        if (b1 != null && b1.getUserData() instanceof Projectile) {
            Projectile p = (Projectile) b1.getUserData();

            if (b2 != null && b2.getUserData() instanceof Alien) {
                Alien alienTouche = (Alien) b2.getUserData();
                if (p.getShooter() == alienTouche && p.getFrameCount() < 30) {
                    return;
                }
            }
            if (p.isActive() && p instanceof Rocket && p.getState() == Projectile.State.FLYING) {
                p.setState(Projectile.State.EXPLODED);
                projectilePhysics.explodeProjectile(p);
                world.removeBody(p.getBody());
                projectilePhysics.getActiveProjectiles().remove(p);
            }
        }
    }
    public Terrain getTerrain() {
        return terrain;
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


////    FONCTIONS AUXILIAIRES    ////

    private boolean isOnGround(Body body) {
        Vector2 vel = body.getLinearVelocity();
        return Math.abs(vel.y) < 3.0;
    }


////    FONCTIONS POUR ALIENS    ////
    public void addAlien(Alien alien) {
        alienPhysics.addAlienBody(alien);
    }

    public void removeAlien(Alien alien) {
        alienPhysics.removeAlien(alien);
    }

    public void jumpAlien(Alien alien) {
        alienPhysics.jumpAlien(alien);
    }


    public void moveAlien(Alien alien, double direction) {
        alienPhysics.moveAlien(alien, direction);
    }

////    FONCTIONS POUR PROJECTILES    ////
    public void addProjectile(Projectile projectile, int angleDegrees, int powerPercent) {
        projectilePhysics.addProjectile(projectile, angleDegrees, powerPercent);
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
        this.wind = new Vector2(windX, windY);
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
}
