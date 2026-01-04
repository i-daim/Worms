package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Capsule;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import upc.assets.SoundManager;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.Terrain;
import upc.model.jeu.TerrainGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AlienPhysics {

    private final World<Body> world;
    private Terrain terrain;


    // Les Maps sont remplacés par une classe qui rassemble les valeurs par alien
    private static class AlienState {
        private final Body body;
        private boolean hasJumped = false;
        private double previousVelocityY = 0.0;

        AlienState(Body body) {
            this.body = body;
        }
    }

    private final Map<Alien, AlienState> alienStates = new HashMap<>();

    private static final double SAFE_FALL_SPEED = 17.5;
    private static final double MAX_FALL_SPEED = 35.0;
    private static final double DMG_MULTIPLIER = 0.75;

    private static final double FRICTION = 0.0;
    private static final double DENSITY = 2;
    private static final double LINEAR_DAMPING = 4.0;

    private static final double ALIEN_WIDTH = Converter.toMeters(18);
    private static final double ALIEN_HEIGHT = Converter.toMeters(25);

    public AlienPhysics(World<Body> world, Terrain terrain) {
        this.world = world;
        this.terrain = terrain;
    }

    public void updateAliens() {
        for (Map.Entry<Alien, AlienState> entry : alienStates.entrySet()) {
            Alien alien = entry.getKey();
            AlienState state = entry.getValue();
            Body body = state.body;

            if (!alien.isAlive()) {
                continue;
            }
            boolean isOnGround = isOnGround(body);
            if (isOnGround) {
                state.hasJumped = false;
            }

            checkFallDamage(alien, state, isOnGround);
            checkAirFriction(body, isOnGround);

            Vector2 v = body.getTransform().getTranslation();
            int pixelX = Converter.toPixels(v.x);
            int pixelY = Converter.toPixels(v.y);
            alien.setPosition(new Position(pixelX, pixelY));
            int tileY = pixelY / TerrainGenerator.TILE_SIZE;
            if (terrain.isWater(tileY)) {
                if (alien.getHealthPoints() > 0) {
                    SoundManager.playEffect("Splash");
                }
            }

            int collisionY = pixelY / TerrainGenerator.TILE_SIZE;
            if (collisionY >= Terrain.getWaterLevel()) {
                alien.receiveDmg(alien.getHealthPoints());
            }

            if (body.getLinearVelocity().getMagnitude() < 0.1) {
                body.setLinearVelocity(0, 0);
                body.setAngularVelocity(0);
            }

            state.previousVelocityY = body.getLinearVelocity().y;
        }
    }

    public void addAlienBody(Alien alien) {
        Body body = createAlienBody(alien);
        this.world.addBody(body);
        this.alienStates.put(alien, new AlienState(body));
    }

    private Body createAlienBody(Alien alien) {
        Body body = new Body();
        Capsule capsuleShape = Geometry.createCapsule(ALIEN_WIDTH, ALIEN_HEIGHT);
        BodyFixture fixture = new BodyFixture(capsuleShape);
        fixture.setFriction(FRICTION);
        fixture.setRestitution(0.0);
        fixture.setDensity(DENSITY);
        body.addFixture(fixture);

        body.setMass(MassType.NORMAL);
        body.setLinearDamping(LINEAR_DAMPING);
        body.setAngularDamping(10.0);

        body.translate(
                Converter.toMeters(alien.getPosition().getX()),
                Converter.toMeters(alien.getPosition().getY())
        );
        body.setUserData(alien);
        return body;
    }

    public void removeAlien(Alien alien) {
        AlienState state = alienStates.remove(alien);
        if (state != null) {
            world.removeBody(state.body);
        }
    }

    public void moveAlien(Alien alien, double direction) {
        AlienState state = alienStates.get(alien);
        if (state != null) {
            double speed = 3.0 * direction;
            double velY = state.body.getLinearVelocity().y;
            state.body.setLinearVelocity(new Vector2(speed, velY));
            state.body.setAtRest(false);
        }
    }

    public void jumpAlien(Alien alien) {
        AlienState state = alienStates.get(alien);
        if (state != null) {
            if (!state.hasJumped) {
                Vector2 vel = state.body.getLinearVelocity();
                state.body.setLinearVelocity(vel.x, -20.0);
                state.body.setAtRest(false);
                state.hasJumped = true;
            }
        }
    }

    public void flyAlien(Alien alien) {
        AlienState state = alienStates.get(alien);
        if (state != null) {
            Vector2 vel = state.body.getLinearVelocity();
            state.body.setLinearVelocity(vel.x, -10.0);
            state.body.setAtRest(false);
            state.hasJumped = true;
        }
    }

    private void checkAirFriction(Body body, boolean isOnGround) {
        body.setLinearDamping(isOnGround ? LINEAR_DAMPING : 0.5);
    }

    private void checkFallDamage(Alien alien, AlienState state, boolean isOnGround) {
        if (isOnGround) {
            double impactSpeed = state.previousVelocityY;
            if (impactSpeed > SAFE_FALL_SPEED) {
                int damage = (int) ((impactSpeed - SAFE_FALL_SPEED) * DMG_MULTIPLIER);
                if (damage > 0) {
                    alien.receiveDmg(damage);
                }
            }
        }
    }

    private boolean isOnGround(Body body) {
        Vector2 pos = body.getTransform().getTranslation();

        double x = pos.x;
        double feetY = pos.y + (ALIEN_HEIGHT / 2) + Converter.toMeters(2);
        double halfWidth = ALIEN_WIDTH / 2;

        return isSolid(x, feetY)
                || isSolid(x - halfWidth + Converter.toMeters(1), feetY)
                || isSolid(x + halfWidth - Converter.toMeters(1), feetY);
    }

    private boolean isSolid(double x, double y) {
        int tileX = Converter.toPixels(x) / TerrainGenerator.TILE_SIZE;
        int tileY = Converter.toPixels(y) / TerrainGenerator.TILE_SIZE;
        boolean[][] collisions = terrain.getCollisions();

        if (tileY >= 0 && tileY < collisions.length
                && tileX >= 0 && tileX < collisions[0].length) {
            return collisions[tileY][tileX];
        }
        return false;
    }

    public void limitFallVelocities() {
        for (AlienState state : alienStates.values()) {
            Vector2 vel = state.body.getLinearVelocity();
            if (vel.y > MAX_FALL_SPEED) {
                state.body.setLinearVelocity(vel.x, MAX_FALL_SPEED);
            }
        }
    }

    public Map<Alien, Body> getAlienBodies() {
        Map<Alien, Body> bodies = new HashMap<>();
        for (Map.Entry<Alien, AlienState> entry : alienStates.entrySet()) {
            bodies.put(entry.getKey(), entry.getValue().body);
        }
        return bodies;
    }

    public Set<Alien> getAliens() {
        return alienStates.keySet();
    }

    public Body getAlienBody(Alien alien) {
        AlienState state = alienStates.get(alien);
        return state != null ? state.body : null;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
}
