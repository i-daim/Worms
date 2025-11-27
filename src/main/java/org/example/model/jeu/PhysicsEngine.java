package org.example.model.jeu;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.entites.Projectile;
import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;


public class PhysicsEngine {
    private final World<Body> world;

    public static final double SCALE = 20.0;
    private final List<Projectile> activeProjectiles = new ArrayList<>();
    private Vector2 wind = new Vector2(0, 0); // Vent (x, y)
    private final double dt = 1.0 / 60.0;
    private final Vector2 gravity = new Vector2(0, 9.8);

    private final Map<Worm, Body> wormBodies = new HashMap<>();

    private final Map<Worm, Boolean> wasInAir = new HashMap<>();
    private final Map<Worm, Double> maxFallSpeed = new HashMap<>();

    private final Terrain terrain;

    private static final double VITESSE_CHUTE_SAFE = 0.5;
    private static final double VITESSE_CHUTE_MAX = 15.0;
    private static final int MULTIPLICATEUR_DEGATS = 10;

    public PhysicsEngine(Terrain terrain) {
        this.world = new World<>();
        this.world.setGravity(gravity);
        this.terrain = terrain;
        createTerrainBodies();
    }
    public void setWind(double windX, double windY) {
        this.wind = new Vector2(windX, windY);
    }
    public Vector2 getWind() {
        return wind;
    }
    public void update() {
        limitFallVelocities();
        world.update(dt);

        for (Map.Entry<Worm, Body> entry : wormBodies.entrySet()) {
            Worm worm = entry.getKey();
            Body body = entry.getValue();

            if (!worm.isAlive()) {
                continue;
            }

            checkFallDamage(worm, body);

            Vector2 v = body.getTransform().getTranslation();
            int pixelX = (int) (v.x * SCALE);
            int pixelY = (int) (v.y * SCALE);
            worm.setPosition(new Position(pixelX, pixelY));

            if (body.getLinearVelocity().getMagnitude() < 0.1) {
                body.setLinearVelocity(0, 0);
                body.setAngularVelocity(0);
            }
        }
    }

    private void limitFallVelocities() {
        for (Body body : wormBodies.values()) {
            Vector2 vel = body.getLinearVelocity();
            if (vel.y > VITESSE_CHUTE_MAX) {
                body.setLinearVelocity(vel.x, VITESSE_CHUTE_MAX);
            }
        }
    }

    private void checkFallDamage(Worm worm, Body body) {
        Vector2 currentVel = body.getLinearVelocity();
        double currentVelY = currentVel.y;
        boolean isOnGround = isOnGround(body);

        boolean wasAirborne = wasInAir.getOrDefault(worm, false);
        double maxSpeed = maxFallSpeed.getOrDefault(worm, 0.0);

        if (wasAirborne && isOnGround && maxSpeed > VITESSE_CHUTE_SAFE) {
            int degats = (int) ((maxSpeed - VITESSE_CHUTE_SAFE) * MULTIPLICATEUR_DEGATS);
            if (degats > 0) {
                worm.receiveDmg(degats);
            }
            maxFallSpeed.put(worm, 0.0);
        }

        if (!isOnGround) {
            double newMaxSpeed = Math.max(maxSpeed, Math.abs(currentVelY));
            maxFallSpeed.put(worm, newMaxSpeed);
        }

        wasInAir.put(worm, !isOnGround);
    }

    private boolean isOnGround(Body body) {
        Vector2 vel = body.getLinearVelocity();
        return Math.abs(vel.y) < 0.5;
    }


    public void addWorm(Worm worm) {
        Body body = new Body();

        double radius = 8.0 / SCALE;

        BodyFixture fixture = new BodyFixture(Geometry.createCircle(radius));
        fixture.setFriction(0.9);
        fixture.setRestitution(0.1);
        fixture.setDensity(1.0);
        body.addFixture(fixture);

        body.setMass(MassType.NORMAL);
        body.setLinearDamping(0.5);
        body.setAngularDamping(0.8);

        body.translate(
                worm.getPosition().getX() / SCALE,
                worm.getPosition().getY() / SCALE
        );

        body.setUserData(worm);

        this.world.addBody(body);
        this.wormBodies.put(worm, body);
        this.wasInAir.put(worm, false);
        this.maxFallSpeed.put(worm, 0.0);
    }

    public void removeWorm(Worm worm) {
        Body body = wormBodies.remove(worm);
        wasInAir.remove(worm);
        maxFallSpeed.remove(worm);
        if (body != null) {
            world.removeBody(body);
        }
    }

    public void jumpWorm(Worm worm) {
        Body body = wormBodies.get(worm);
        if (body != null && isOnGround(body)) {
            body.applyImpulse(new Vector2(0, -6.5));
        }
    }

    public void moveWorm(Worm worm, double direction) {
        Body body = wormBodies.get(worm);
        if (body != null) {
            double speed = 2.0 * direction;
            double velY = body.getLinearVelocity().y;
            body.setLinearVelocity(new Vector2(speed, velY));
            body.setAtRest(false);
        }
    }

    private void createTerrainBodies() {
        boolean[][] collisions = terrain.getCollisions();
        int height = collisions.length;
        int width = collisions[0].length;

        for (int y = 0; y < height; y++) {
            int startX = -1;
            for (int x = 0; x <= width; x++) {
                boolean isCollision = (x < width) && collisions[y][x];

                if (isCollision && startX == -1) {
                    startX = x;
                } else if (!isCollision && startX != -1) {
                    createTerrainSegment(startX, y, x - startX, 1);
                    startX = -1;
                }
            }
        }
    }

    private void createTerrainSegment(int x, int y, int w, int h) {
        Body terrainBody = new Body();

        double centerX = (x + w / 2.0) / SCALE;
        double centerY = (y + h / 2.0) / SCALE;
        double width = w / SCALE;
        double height = h / SCALE;

        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(width, height));
        fixture.setFriction(0.8);
        fixture.setRestitution(0.1);
        terrainBody.addFixture(fixture);
        terrainBody.translate(centerX, centerY);
        terrainBody.setMass(MassType.INFINITE);

        world.addBody(terrainBody);
    }

    public World<Body> getWorld() {
        return world;
    }

    public Body getWormBody(Worm worm) {
        return wormBodies.get(worm);
    }
}
