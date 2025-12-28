package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Capsule;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.Terrain;
import upc.model.jeu.TerrainGenerator;

import java.util.HashMap;
import java.util.Map;

public class AlienPhysics {

    private final World<Body> world;
    private final Terrain terrain;

    private final Map<Alien, Body> alienBodies = new HashMap<>();
    private final Map<Alien, Boolean> wasInAir = new HashMap<>();
    private final Map<Alien, Double> maxFallSpeed = new HashMap<>();
    private final Map<Alien, Boolean> hasJumped = new HashMap<>();
    private final Map<Alien, Double> previousVelocities = new HashMap<>();
    private final Map<Alien, Integer> groundBuffer = new HashMap<>();
    private final Map<Alien, Boolean> isFalling = new HashMap<>();


    private static final double SAFE_FALL_SPEED = 15.0;
    private static final double MAX_FALL_SPEED = 20.0;
    private static final int DMG_MULTIPLIER = 1;
    private static final int GROUND_BUFFER_FRAMES = 5;

    private static final double FRICTION = 0.0;
    private static final double DENSITY = 1.2;
    private static final double LINEAR_DAMPING = 4.0;

    private static final double ALIEN_WIDTH = Converter.toMeters(20);
    private static final double ALIEN_HEIGHT = Converter.toMeters(37);

    public AlienPhysics(World<Body> world, Terrain terrain) {
        this.world = world;
        this.terrain = terrain;
    }

    public void updateAliens() {
        for (Map.Entry<Alien, Body> entry : alienBodies.entrySet()) {
            Alien alien = entry.getKey();
            Body body = entry.getValue();

            if (!alien.isAlive()) {
                continue;
            }
            boolean isOnGround = isOnGround(body);
            if (isOnGround) {
                hasJumped.put(alien, false);
            }
            if (isOnGround) {
                groundBuffer.put(alien, GROUND_BUFFER_FRAMES);
            } else {
                groundBuffer.put(alien, Math.max(0, groundBuffer.get(alien) - 1));
            }

            checkFallDamage(alien, body, isOnGround);
            checkAirFriction(body, isOnGround);

            Vector2 v = body.getTransform().getTranslation();
            int pixelX = Converter.toPixels(v.x);
            int pixelY = Converter.toPixels(v.y);
            alien.setPosition(new Position(pixelX, pixelY));

            int collisionY = pixelY / TerrainGenerator.TILE_SIZE;
            if (collisionY >= Terrain.getWaterLevel()) {
                alien.receiveDmg(alien.getHealthPoints());
            }

            if (body.getLinearVelocity().getMagnitude() < 0.1) {
                body.setLinearVelocity(0, 0);
                body.setAngularVelocity(0);
            }

            previousVelocities.put(alien, body.getLinearVelocity().y);
        }
    }

    public void addAlienBody(Alien alien) {
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

        this.world.addBody(body);
        this.alienBodies.put(alien, body);
        this.wasInAir.put(alien, false);
        this.maxFallSpeed.put(alien, 0.0);
        this.hasJumped.put(alien, false);
        this.previousVelocities.put(alien, 0.0);
        groundBuffer.put(alien, 0);
        isFalling.put(alien, false);
    }

    public void removeAlien(Alien alien) {
        wasInAir.remove(alien);
        maxFallSpeed.remove(alien);
        hasJumped.remove(alien);
        previousVelocities.remove(alien);
        groundBuffer.remove(alien);
        isFalling.remove(alien);
        alienBodies.remove(alien);
    }

    public void moveAlien(Alien alien, double direction) {
        Body body = alienBodies.get(alien);
        if (body != null) {
            double speed = 3.0 * direction;
            double velY = body.getLinearVelocity().y;
            body.setLinearVelocity(new Vector2(speed, velY));
            body.setAtRest(false);
        }
    }

    public void jumpAlien(Alien alien) {
        Body body = alienBodies.get(alien);
        if (body != null && groundBuffer.getOrDefault(alien, 0) > 0) {
            if (!hasJumped.getOrDefault(alien, false)) {
                body.applyImpulse(new Vector2(0, -45.0));
                body.setAtRest(false);
                hasJumped.put(alien, true);
                isFalling.put(alien, false);
            }
        } else {
            hasJumped.put(alien, false);
        }
    }

    private void checkAirFriction(Body body, boolean isOnGround) {
        if (!isOnGround) {
            body.setLinearDamping(0.5);
        } else {
            body.setLinearDamping(LINEAR_DAMPING);
        }
    }

    public void checkFallDamage(Alien alien, Body body, boolean isOnGround) {
        Vector2 currentVel = body.getLinearVelocity();
        double currentVelY = currentVel.y;


        boolean wasAirborne = wasInAir.getOrDefault(alien, false);
        double maxSpeed = maxFallSpeed.getOrDefault(alien, 0.0);
        double prevVelY = previousVelocities.getOrDefault(alien, 0.0);

        if (isOnGround && isFalling.getOrDefault(alien, false)) {

            if (maxSpeed > SAFE_FALL_SPEED) {
                int degats = (int) ((maxSpeed - SAFE_FALL_SPEED) * DMG_MULTIPLIER);
                if (degats > 0) {
                    alien.receiveDmg(degats);
                }
            }
            maxFallSpeed.put(alien, 0.0);
            isFalling.put(alien, false); // reset chute
            hasJumped.put(alien, false);
        }


        if (!isOnGround && currentVelY > SAFE_FALL_SPEED) {
            isFalling.put(alien, true);
            double newMaxSpeed = Math.max(maxSpeed, currentVelY);
            maxFallSpeed.put(alien, newMaxSpeed);
        }

    }


    private boolean isOnGround(Body body) {
            Vector2 pos = body.getTransform().getTranslation();
            int centerX = Converter.toPixels(pos.x);
            int centerY = Converter.toPixels(pos.y);
            int pixelWidth = Converter.toPixels(ALIEN_WIDTH);
            int pixelHeight = Converter.toPixels(ALIEN_HEIGHT);
            int feetY = centerY + (pixelHeight / 2) + 2; // Petite Tolerance
            int tileY = feetY / TerrainGenerator.TILE_SIZE;
            boolean[][] collisions = terrain.getCollisions();
            if (tileY < 0 || tileY >= collisions.length) {
                return false;
            }
            int leftX = centerX - (pixelWidth / 2) + 1;
            int rightX = centerX + (pixelWidth / 2) - 1;

            int[] checkXs = new int[] {leftX, centerX, rightX};
            int width = collisions[0].length;
            for (int px : checkXs) {
                int tileX = px / TerrainGenerator.TILE_SIZE;
                if (tileX < 0 || tileX >= width) {
                    continue;
                }
                if (collisions[tileY][tileX]) {
                    return true;
                }
            }

            return false;
    }

    public void limitFallVelocities() {
        for (Body body : alienBodies.values()) {
            Vector2 vel = body.getLinearVelocity();
            if (vel.y > MAX_FALL_SPEED) {
                body.setLinearVelocity(vel.x, MAX_FALL_SPEED);
            }
        }
    }

    public Map<Alien, Body> getAlienBodies() {
        return alienBodies;
    }

    public Body getAlienBody(Alien alien) {
        return alienBodies.get(alien);
    }
}