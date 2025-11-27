package org.example.model.jeu;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import org.example.model.entites.Position;
import org.example.model.entites.Worm;

import java.util.HashMap;
import java.util.Map;

public class PhysicsEngine {
    private final World<Body> world;

    public static final double SCALE = 20.0;

    private final double dt = 1.0 / 60.0;
    private final Vector2 gravity = new Vector2(0, 9.8);

    private final Map<Worm, Body> wormBodies = new HashMap<>();
    private final Map<Worm, WormPhysicsState> wormStates = new HashMap<>();

    private final Terrain terrain;

    private static final double VITESSE_CHUTE_SAFE = 8.0;
    private static final double VITESSE_CHUTE_MAX = 15.0;
    private static final int MULTIPLICATEUR_DEGATS = 3;

    public PhysicsEngine(Terrain terrain) {
        this.world = new World<>();
        this.world.setGravity(gravity);
        this.terrain = terrain;
        createTerrainBodies();
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
        WormPhysicsState state = wormStates.get(worm);
        if (state == null) {
            state = new WormPhysicsState();
            wormStates.put(worm, state);
        }

        Vector2 currentVel = body.getLinearVelocity();
        double currentVelY = currentVel.y;
        boolean isOnGround = isOnGround(body);

        if (state.wasInAir && isOnGround && state.maxFallSpeed > VITESSE_CHUTE_SAFE) {
            int degats = (int) ((state.maxFallSpeed - VITESSE_CHUTE_SAFE) * MULTIPLICATEUR_DEGATS);
            if (degats > 0) {
                worm.receiveDmg(degats);
                state.lastFallDamage = degats;
                state.lastFallSpeed = state.maxFallSpeed;
                state.hasTakenFallDamage = true;
            }
            state.maxFallSpeed = 0;
        }

        if (!isOnGround) {
            state.maxFallSpeed = Math.max(state.maxFallSpeed, Math.abs(currentVelY));
        }
        state.wasInAir = !isOnGround;
    }

    private boolean isOnGround(Body body) {
        Vector2 vel = body.getLinearVelocity();
        return Math.abs(vel.y) < 0.5 && body.getInContactBodies(false).size() > 0;
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
        this.wormStates.put(worm, new WormPhysicsState());
    }





}
