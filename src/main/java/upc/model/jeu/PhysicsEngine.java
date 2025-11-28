package upc.model.jeu;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.entites.Projectile;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import java.util.HashMap;
import java.util.Map;


public class PhysicsEngine {
    private final World<Body> world;

    public static final double SCALE = 20.0;
    private final List<Projectile> activeProjectiles = new ArrayList<>();
    private Vector2 wind = new Vector2(0, 0); // Vent (x, y)
    private final double dt = 1.0 / 60.0;
    private final Vector2 gravity = new Vector2(0, 60.0);


    private final Map<Alien, Body> wormBodies = new HashMap<>();

    private final Map<Alien, Boolean> wasInAir = new HashMap<>();
    private final Map<Alien, Double> maxFallSpeed = new HashMap<>();

    private final Terrain terrain;

    private static final double SAFE_FALL_SPEED = 15;
    private static final double MAX_FALL_SPEED = 20.0;
    private static final int DMG_MULTIPLIER = 1;

    private static final double WORM_WIDTH = Converter.toMeters(20);
    private static final double WORM_HEIGHT = Converter.toMeters(37);

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

        for (Map.Entry<Alien, Body> entry : wormBodies.entrySet()) {
            Alien alien = entry.getKey();
            Body body = entry.getValue();

            if (!alien.isAlive()) {
                continue;
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
        updateProjectiles();
    }

    private void limitFallVelocities() {
        for (Body body : wormBodies.values()) {
            Vector2 vel = body.getLinearVelocity();
            if (vel.y > MAX_FALL_SPEED) {
                body.setLinearVelocity(vel.x, MAX_FALL_SPEED);
            }
        }
    }

    private void checkFallDamage(Alien alien, Body body) {
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


    public void addWorm(Alien alien) {
        Body body = new Body();

        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(WORM_WIDTH, WORM_HEIGHT));
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

        this.world.addBody(body);
        this.wormBodies.put(alien, body);
        this.wasInAir.put(alien, false);
        this.maxFallSpeed.put(alien, 0.0);
    }

    public void removeWorm(Alien alien) {
        Body body = wormBodies.remove(alien);
        wasInAir.remove(alien);
        maxFallSpeed.remove(alien);
        if (body != null) {
            world.removeBody(body);
        }
    }

    public void jumpWorm(Alien alien) {
        Body body = wormBodies.get(alien);
        if (body != null && isOnGround(body)) {
            body.applyImpulse(new Vector2(0, -45.0));
            body.setAtRest(false);
        }
    }


    public void moveWorm(Alien alien, double direction) {
        Body body = wormBodies.get(alien);
        if (body != null) {
            double speed = 3.0 * direction;
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

    private void createTerrainSegment(int tileX, int tileY, int tileWidth, int tileHeight) {
        Body terrainBody = new Body();

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

        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(meterWidth, meterHeight));
        fixture.setFriction(0.8);
        fixture.setRestitution(0.1);
        terrainBody.addFixture(fixture);
        terrainBody.translate(meterCenterX, meterCenterY);
        terrainBody.setMass(MassType.INFINITE);

        world.addBody(terrainBody);
    }

    public World<Body> getWorld() {
        return world;
    }

    public Body getWormBody(Alien alien) {
        return wormBodies.get(alien);
    }

    public void addProjectile(Projectile projectile, int angleDegrees, int powerPercent) {
        Body body = new Body();

        double radius = 5.0 / SCALE;
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(radius));
        fixture.setFriction(0.3);
        fixture.setRestitution(0.3);
        fixture.setDensity(1.5);

        body.addFixture(fixture);
        body.setMass(MassType.NORMAL);
        body.setBullet(true);

        double startX = projectile.getPosition().getX() / SCALE;
        double startY = projectile.getPosition().getY() / SCALE;
        body.translate(startX, startY);

        double angleRad = Math.toRadians(angleDegrees);
        double baseVelocity = projectile.getWeapon().getInitialVelocity();
        double velocityMagnitude = (baseVelocity * powerPercent) / 100.0;

        double vx = velocityMagnitude * Math.cos(angleRad);
        double vy = -velocityMagnitude * Math.sin(angleRad);

        body.setLinearVelocity(vx, vy);
        body.setUserData(projectile);

        projectile.setBody(body);
        this.world.addBody(body);
        this.activeProjectiles.add(projectile);
    }
    private void applyWindToProjectiles() {
        for (Projectile proj : activeProjectiles) {
            if (proj.isActive() && proj.getWeapon().isAffectedByWind()) {
                Body body = proj.getBody();
                if (body != null) {
                    Vector2 windForce = wind.product(0.5); // 50% de la force du vent
                    body.applyForce(windForce);
                }
            }
        }
    }
    private void explodeProjectile(Projectile proj) {
        int explosionX = proj.getPosition().getX();
        int explosionY = proj.getPosition().getY();
        int radius = proj.getWeapon().getRayonExplosion();
        int damage = proj.getWeapon().getDegats();

        // Infliger dégâts aux worms proches
        for (Map.Entry<Alien, Body> entry : wormBodies.entrySet()) {
            Alien alien = entry.getKey();
            if (!alien.isAlive()) {
                continue;
            }

            Position wormPos = alien.getPosition();
            double distance = wormPos.distance(explosionX, explosionY);

            if (distance <= radius * SCALE) {
                // Dégâts diminuent avec la distance
                double damageRatio = 1.0 - (distance / (radius * SCALE));
                int finalDamage = (int) (damage * damageRatio);

                if (finalDamage > 0) {
                    alien.receiveDmg(finalDamage);
                }

                // Onde de choc (pousse les worms)
                Body wormBody = entry.getValue();
                if (wormBody != null && distance > 0) {
                    double knockbackForce = 50.0 * damageRatio;
                    double dx = (wormPos.getX() - explosionX) / distance;
                    double dy = (wormPos.getY() - explosionY) / distance;
                    wormBody.applyImpulse(new Vector2(dx * knockbackForce, dy * knockbackForce));
                }
            }
        }
    }
    private void updateProjectiles() {
        Iterator<Projectile> iterator = activeProjectiles.iterator();

        while (iterator.hasNext()) {
            Projectile proj = iterator.next();
            Body body = proj.getBody();

            if (body == null) {
                iterator.remove();
                continue;
            }

            proj.incrementFrame();

            Vector2 pos = body.getTransform().getTranslation();
            int x = (int) (pos.x * SCALE);
            int y = (int) (pos.y * SCALE);
            proj.setPosition(new Position(x, y));

            if (
                    y > terrain.getCollisionsHeight() + 100
                            || x < -50
                            || x > terrain.getCollisionsWidth() + 50
            ) {
                proj.setState(Projectile.ProjectileState.OUT_OF_BOUNDS);
                world.removeBody(body);
                iterator.remove();
                continue;
            }

            if (body.getLinearVelocity().getMagnitude() < 1.0 && proj.getFrameCount() > 10) {
                proj.setState(Projectile.ProjectileState.EXPLODED);
                explodeProjectile(proj);
                world.removeBody(body);
                iterator.remove();
            }
        }
    }
    public List<Projectile> getActiveProjectiles() {
        return new ArrayList<>(activeProjectiles);
    }

}
