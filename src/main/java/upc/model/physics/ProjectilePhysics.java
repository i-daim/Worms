package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.entites.Projectile;
import upc.model.jeu.Terrain;
import upc.model.entites.ProjectileBalle;
import upc.model.entites.ProjectileMine;
import upc.model.entites.ProjectileGrenade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectilePhysics {

    private final PhysicsEngine physicsEngine;
    private final World<Body> world;
    private final List<Projectile> activeProjectiles;

    private Vector2 wind = new Vector2(0, 0); // Vent (x, y)


    public ProjectilePhysics(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
        this.world = physicsEngine.getWorld();
        this.activeProjectiles = new ArrayList<>();
    }


    public Body addProjectile(Projectile projectile, int angleDegrees, int powerPercent) {
        Body body = new Body();
        if (projectile instanceof ProjectileGrenade) {
            // GRENADE : Rebondit, roule, soumise à la gravité normale
            double radius = Converter.toMeters(5);
            BodyFixture fixture = new BodyFixture(Geometry.createCircle(radius));
            fixture.setFriction(1.0);      // Friction élevée pour rouler
            fixture.setRestitution(0.5);   // Rebondit à 50% de sa vitesse
            fixture.setDensity(2.0);       // Assez dense
            body.addFixture(fixture);
            body.setMass(MassType.NORMAL);

        } else if (projectile instanceof ProjectileBalle) {
            // BALLE (Uzi/Shotgun) : Pas de rebond, très légère, trajectoire tendue
            double radius = Converter.toMeters(2); // Tout petit
            BodyFixture fixture = new BodyFixture(Geometry.createCircle(radius));
            fixture.setFriction(0.0);
            fixture.setRestitution(0.0);   // S'écrase, ne rebondit pas
            fixture.setDensity(0.5);       // Légère
            body.addFixture(fixture);
            body.setMass(MassType.NORMAL);
            body.setBullet(true);          // CCD pour éviter de traverser les murs à haute vitesse

        } else if (projectile instanceof ProjectileMine) {
            // MINE : Forme rectangulaire, se pose, très lourde
            double width = Converter.toMeters(10);
            double height = Converter.toMeters(5);
            BodyFixture fixture = new BodyFixture(Geometry.createRectangle(width, height));
            fixture.setFriction(5.0);      // Très rugueux, ne doit pas glisser
            fixture.setRestitution(0.0);   // Pas de rebond
            fixture.setDensity(10.0);      // Très lourd pour ne pas être poussé facilement
            body.addFixture(fixture);
            body.setMass(MassType.NORMAL);

        } else {
            // DEFAUT (Roquette) : Cercle standard, explose au contact
            double radius = Converter.toMeters(5);
            BodyFixture fixture = new BodyFixture(Geometry.createCircle(radius));
            fixture.setFriction(0.3);
            fixture.setRestitution(0.0);
            fixture.setDensity(1.5);
            body.addFixture(fixture);
            body.setMass(MassType.NORMAL);
            body.setBullet(true); // Pour éviter de traverser les murs
        }

        double startX = Converter.toMeters(projectile.getPosition().getX());
        double startY = Converter.toMeters(projectile.getPosition().getY());
        body.translate(startX, startY);

        double angleRad = Math.toRadians(angleDegrees);
        double baseVelocity = projectile.getWeapon().getPuissanceTir();
        double velocityMagnitude = (baseVelocity * powerPercent) / 100.0;

        double vx = velocityMagnitude * Math.cos(angleRad);
        double vy = -velocityMagnitude * Math.sin(angleRad);

        body.setLinearVelocity(vx, vy);
        body.setUserData(projectile); // Lien vers l'objet logique

        projectile.setBody(body);
        world.addBody(body);
        activeProjectiles.add(projectile);

        return body;

    }

    private void applyWindToProjectiles() {
        for (Projectile proj : activeProjectiles) {
            if (proj.isActive()) {
                Body body = proj.getBody();
                if (body != null) {
                    Vector2 windForce = wind.product(0.5); // 50% de la force du vent
                    body.applyForce(windForce);
                }
            }
        }
    }

    public void updateProjectiles(Terrain terrain) {
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
            int x = Converter.toPixels(pos.x);
            int y = Converter.toPixels(pos.y);
            proj.setPosition(new Position(x, y));

            if (
                    y > terrain.getCollisionsHeight() + 100
                            || x < -50
                            || x > terrain.getCollisionsWidth() + 50
            ) {
                proj.setState(Projectile.State.OUT_OF_BOUNDS);
                world.removeBody(body);
                iterator.remove();
                continue;
            }

            if (body.getLinearVelocity().getMagnitude() < 1.0 && proj.getFrameCount() > 10) {
                proj.setState(Projectile.State.EXPLODED);
                explodeProjectile(proj);
                world.removeBody(body);
                iterator.remove();
            }
        }
    }

    public void explodeProjectile(Projectile proj) {
        int explosionX = proj.getPosition().getX();
        int explosionY = proj.getPosition().getY();
        int radius = proj.getExplosionRadius();
        int damage = proj.getDamage();

        // Infliger dégâts aux worms proches
        for (Alien alien : physicsEngine.getAlienBodies().keySet()) {
            if (!alien.isAlive()) {
                continue;
            }

            Position wormPos = alien.getPosition();
            double distance = wormPos.distance(explosionX, explosionY);

            if (distance <= Converter.toPixels(radius)) {
                // Dégâts diminuent avec la distance
                double damageRatio = 1.0 - (distance / Converter.toPixels(radius));
                int finalDamage = (int) (damage * damageRatio);

                if (finalDamage > 0) {
                    alien.receiveDmg(finalDamage);
                }

                // Onde de choc (pousse les worms)
                Body wormBody = physicsEngine.getAlienBodies().get(alien);
                if (wormBody != null && distance > 0) {
                    double knockbackForce = 50.0 * damageRatio;
                    double dx = (wormPos.getX() - explosionX) / distance;
                    double dy = (wormPos.getY() - explosionY) / distance;
                    wormBody.applyImpulse(new Vector2(dx * knockbackForce, dy * knockbackForce));
                }
            }
        }
    }

    public List<Projectile> getActiveProjectiles() {
        return activeProjectiles;
    }
}