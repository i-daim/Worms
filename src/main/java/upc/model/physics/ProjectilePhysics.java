package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.Terrain;
import upc.model.entites.projectiles.Bullet;
import upc.model.entites.projectiles.Trap;
import upc.model.entites.projectiles.Throwable;
import upc.model.entites.projectiles.Rocket;

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

    /**
     * Crée et configure un corps physique pour un projectile,
     * puis l'ajoute au monde physique et à la liste des projectiles actifs.
     *
     * @param projectile    le projectile à lancer
     * @param angleDegrees  l'angle de tir en degrés (0° vers la droite, 90° vers le haut)
     * @param powerPercent  la puissance du tir en pourcentage (0 à 100)
     * @return le corps physique (Body) associé au projectile
     */
    public Body addProjectile(Projectile projectile, int angleDegrees, int powerPercent) {
        Body body = new Body();
        // Cas 1 : projectile « Throwable » (grenades, etc.)
        if (projectile instanceof Throwable) {
            double radius = Converter.toMeters(5);
            BodyFixture fixture = new BodyFixture(Geometry.createCircle(radius));
            fixture.setFriction(1.0);
            fixture.setRestitution(0.5);
            fixture.setDensity(2.0);
            body.addFixture(fixture);
            body.setMass(MassType.NORMAL);

        } else if (projectile instanceof Bullet) {
            // Cas 2 : projectile de type Bullet (balle, tir rapide)
            double radius = Converter.toMeters(2);
            BodyFixture fixture = new BodyFixture(Geometry.createCircle(radius));
            fixture.setFriction(0.0);
            fixture.setRestitution(0.0);
            fixture.setDensity(0.5);
            body.addFixture(fixture);
            body.setMass(MassType.NORMAL);
            body.setBullet(true);

        } else if (projectile instanceof Trap) {
            // Cas 3 : projectile de type Trap (mine, piège posé au sol)
            double width = Converter.toMeters(10);
            double height = Converter.toMeters(5);
            BodyFixture fixture = new BodyFixture(Geometry.createRectangle(width, height));
            fixture.setFriction(5.0);
            fixture.setRestitution(0.0);
            fixture.setDensity(10.0);
            body.addFixture(fixture);
            body.setMass(MassType.NORMAL);

        } else if (projectile instanceof Rocket) {
            // Cas 4 : projectile de type Rocket (roquette)
            double radius = Converter.toMeters(5);
            BodyFixture fixture = new BodyFixture(Geometry.createCircle(radius));
            fixture.setFriction(0.3);
            fixture.setRestitution(0.0);
            fixture.setDensity(2.5);
            body.addFixture(fixture);
            body.setMass(MassType.NORMAL);
            body.setBullet(true);
        }


        double startX = Converter.toMeters(projectile.getPosition().getX());
        double startY = Converter.toMeters(projectile.getPosition().getY());
        body.translate(startX, startY);

        double angleRad = Math.toRadians(angleDegrees);
        double baseVelocity = projectile.getWeapon().getSpeed();
        double velocityMagnitude = (baseVelocity * powerPercent) / 100.0;

        double vx = velocityMagnitude * Math.cos(angleRad);
        double vy = -velocityMagnitude * Math.sin(angleRad);

        body.setLinearVelocity(vx, vy);
        body.setUserData(projectile);

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
    /**
     * Met à jour l'état de tous les projectiles actifs à chaque frame.
     * <p>
     * Cette méthode effectue plusieurs tâches essentielles :
     * <ul>
     *     <li>Synchronise la position du projectile logique avec celle du corps physique.</li>
     *     <li>Vérifie si le projectile est sorti des limites de la carte (Out of Bounds).</li>
     *     <li>Détecte si le projectile s'est arrêté (vitesse quasi nulle) pour déclencher son
     *     explosion.</li>
     *     <li>Nettoie la liste des projectiles actifs en retirant ceux qui sont morts
     *     ou sortis.</li>
     * </ul>
     *
     * @param terrain  Le terrain de jeu actuel, utilisé pour vérifier les limites de la carte.
     */
    public void updateProjectiles(Terrain terrain) {
        // L'itérateur permet de supprimer des éléments en toute sécurité pendant la boucle.
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
    /**
     * Gère la logique d'explosion d'un projectile.
     * <p>
     * Cette méthode applique les conséquences de l'explosion :
     * <ul>
     *     <li>Calcule les dégâts infligés aux Aliens en fonction de leur distance
     *     à l'impact.</li>
     *     <li>Applique une force de recul (knockback) physique aux Aliens touchés.</li>
     *     <li>(Note : La destruction du terrain est généralement gérée ailleurs
     *     ou pourrait être ajoutée ici).</li>
     * </ul>
     *
     * @param proj Le projectile qui vient d'exploser.
     */
    public void explodeProjectile(Projectile proj) {
        //Récupère les coordonnées du centre de l'explosion
        // (position actuelle du/des projectiles en pixels)
        int explosionX = proj.getPosition().getX();
        int explosionY = proj.getPosition().getY();
        int radius = proj.getExplosionRadius();
        int damage = proj.getDamage();

        for (Alien alien : physicsEngine.getAlienBodies().keySet()) {
            // Ignore les aliens déjà morts (pas besoin de les blesser ou de les propulser)
            if (!alien.isAlive()) {
                continue;
            }

            Position wormPos = alien.getPosition();
            double distance = wormPos.distance(explosionX, explosionY);

            if (distance <= Converter.toPixels(radius)) {
                // Calcul de l'atténuation des dégâts (Falloff linéaire)
                double damageRatio = 1.0 - (distance / Converter.toPixels(radius));
                int finalDamage = (int) (damage * damageRatio);

                if (finalDamage > 0) {
                    alien.receiveDmg(finalDamage);
                }

                Body wormBody = physicsEngine.getAlienBodies().get(alien);
                if (wormBody != null && distance > 0) {
                    double knockbackForce = 50.0 * damageRatio;
                    double dx = (wormPos.getX() - explosionX) / distance;
                    double dy = (wormPos.getY() - explosionY) / distance;
                    // Applique une impulsion immédiate au corps physique pour le projeter en l'air
                    wormBody.applyImpulse(new Vector2(dx * knockbackForce, dy * knockbackForce));
                }
            }
        }
    }

    public List<Projectile> getActiveProjectiles() {
        return activeProjectiles;
    }
}