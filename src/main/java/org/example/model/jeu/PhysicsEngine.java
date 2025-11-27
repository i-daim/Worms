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

    // Conversion : 1 mètre physique = 20 pixels à l'écran
    public static final double SCALE = 20.0;

    private final double dt = 1.0 / 60.0; // 60 updates par seconde
    private final Vector2 gravity = new Vector2(0, 9.8);

    // Map pour lier un Worm (Modèle jeu) à son Body (Modèle physique)
    private final Map<Worm, Body> wormBodies = new HashMap<>();
    private final Map<Worm, WormPhysicsState> wormStates = new HashMap<>();

    private final Terrain terrain;

    private static final double VITESSE_CHUTE_SAFE = 8.0; // m/s
    private static final double VITESSE_CHUTE_MAX = 15.0; // Vitesse terminale
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

            if (state.wasInAir && isOnGround && state.maxFallSpeed > VITESSE_CHUTE_SAFE)


        }
