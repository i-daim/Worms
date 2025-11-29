package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.Converter;

import java.util.HashMap;
import java.util.Map;

public class AlienPhysics {

    private final World<Body> world;

    private final Map<Alien, Body> alienBodies = new HashMap<>();
    private final Map<Alien, Boolean> wasInAir = new HashMap<>();
    private final Map<Alien, Double> maxFallSpeed = new HashMap<>();

    private static final double SAFE_FALL_SPEED = 15.0;
    private static final double MAX_FALL_SPEED = 20.0;
    private static final int DMG_MULTIPLIER = 1;

    private static final double ALIEN_WIDTH = Converter.toMeters(20);
    private static final double ALIEN_HEIGHT = Converter.toMeters(37);

    public AlienPhysics(PhysicsEngine physicsEngine) {
        this.world = physicsEngine.getWorld();
    }


    public void updateAlien(Alien alien, Body body) {
        if (!alien.isAlive()) {
            return;
        }

        checkFallDamage(alien, body);

        Vector2 v = body.getTransform().getTranslation();
        int pixelX = Converter.toPixels(v.x);
        int pixelY = Converter.toPixels(v.y);
        alien.setPosition(new Position(pixelX, pixelY));

        if (body.getLinearVelocity().getMagnitude() < 0.1) {
            body.setLinearVelocity(0, 0);
            body.setAngularVelocity(0);
        }
    }

    public Body createAlienBody(Alien alien, World<Body> world) {
        Body body = new Body();

        BodyFixture fixture = new BodyFixture(
                Geometry.createRectangle(ALIEN_WIDTH, ALIEN_HEIGHT));
        fixture.setFriction(0.9);
        fixture.setRestitution(0.1);
        fixture.setDensity(1.2);
        body.addFixture(fixture);

        body.setMass(MassType.NORMAL);
        body.setLinearDamping(0.005);
        body.setAngularDamping(0.8);

        body.translate(
                Converter.toMeters(alien.getPosition().getX()),
                Converter.toMeters(alien.getPosition().getY())
        );

        body.setUserData(alien);

        world.addBody(body);
        this.wasInAir.put(alien, false);
        this.maxFallSpeed.put(alien, 0.0);

        return body;
    }

    public void removeAlien(Alien alien) {
        wasInAir.remove(alien);
        maxFallSpeed.remove(alien);
    }

    public void moveAlien(Body body, double direction) {
        if (body != null) {
            double speed = 3.0 * direction;
            double velY = body.getLinearVelocity().y;
            body.setLinearVelocity(new Vector2(speed, velY));
            body.setAtRest(false);
        }
    }

    public void jumpAlien(Body body) {
        if (body != null && isOnGround(body)) {
            body.applyImpulse(new Vector2(0, -45.0));
            body.setAtRest(false);
        }
    }

    public void checkFallDamage(Alien alien, Body body) {
        Vector2 currentVel = body.getLinearVelocity();
        double currentVelY = currentVel.y;
        boolean isOnGround = isOnGround(body);

        boolean wasAirborne = wasInAir.getOrDefault(alien, false);
        double maxSpeed = maxFallSpeed.getOrDefault(alien, 0.0);

        if (wasAirborne && isOnGround && maxSpeed > SAFE_FALL_SPEED) {
            int degats = (int) ((maxSpeed - SAFE_FALL_SPEED) * DMG_MULTIPLIER);
            if (degats > 0) {
                alien.receiveDmg(degats);
            }
            maxFallSpeed.put(alien, 0.0);
        }

        if (!isOnGround) {
            double newMaxSpeed = Math.max(maxSpeed, Math.abs(currentVelY));
            maxFallSpeed.put(alien, newMaxSpeed);
        }

        wasInAir.put(alien, !isOnGround);
    }

    private boolean isOnGround(Body body) {
        Vector2 vel = body.getLinearVelocity();
        return Math.abs(vel.y) < 3.0;
    }
}
