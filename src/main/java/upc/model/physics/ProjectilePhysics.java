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
import upc.model.entites.projectiles.Throwable;
import upc.model.entites.projectiles.Rocket;
import upc.model.entites.projectiles.Trap;
import upc.model.items.Grenade;
import upc.model.items.Mine;
import upc.model.items.Shotgun;
import upc.model.items.Uzi;
import upc.model.items.Weapon;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Terrain;
import upc.model.entites.projectiles.Bullet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectilePhysics {

    private final PhysicsEngine physicsEngine;
    private final World<Body> world;
    private final List<Projectile> activeProjectiles;
    private final GameWorld gameWorld;

    private Vector2 wind = new Vector2(0, 0); // Vent (x, y)


    public ProjectilePhysics(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
        this.world = physicsEngine.getWorld();
        this.activeProjectiles = new ArrayList<>();
        this.gameWorld = physicsEngine.getGameWorld();
    }

    /**
     * Crée et configure un corps physique pour un projectile,
     * puis l'ajoute au monde physique et à la liste des projectiles actifs.
     *
     * @param projectile    le projectile à lancer
     * @param angleDegrees  l'angle de tir en degrés (0° vers la droite, 90° vers le haut)
     * @param powerPercent  la puissance du tir en pourcentage (0 à 100)
     */
    public void addProjectile(Projectile projectile, int angleDegrees, int powerPercent) {
        Body body = new Body();
        BodyFixture fixture = null;

        Weapon weapon = projectile.getWeapon();

        if (weapon instanceof Grenade) {
            double radius = Converter.toMeters(5);
            fixture = new BodyFixture(Geometry.createCircle(radius));
            fixture.setFriction(1.0);
            fixture.setRestitution(0.5);
            fixture.setDensity(2.0);

        } else if (weapon instanceof Uzi) {
            double radius = Converter.toMeters(2);
            fixture = new BodyFixture(Geometry.createCircle(radius));
            fixture.setFriction(0.1);
            fixture.setRestitution(0.1);
            fixture.setDensity(1.0);
            body.setBullet(true);

        } else if (weapon instanceof Mine) {
            double width = Converter.toMeters(10);
            double height = Converter.toMeters(5);
            fixture = new BodyFixture(Geometry.createRectangle(width, height));
            fixture.setFriction(5.0);
            fixture.setRestitution(0.0);
            fixture.setDensity(10.0);

        }  else if (weapon instanceof Shotgun) {
            double radius = Converter.toMeters(3);
            fixture = new BodyFixture(Geometry.createCircle(radius));
            fixture.setFriction(10.0);
            fixture.setRestitution(0.0);
            fixture.setDensity(5.0);
            body.setLinearDamping(0.8);
            body.setBullet(true);

        } else if (projectile instanceof Rocket) {
            double radius = Converter.toMeters(5);
            fixture = new BodyFixture(Geometry.createCircle(radius));
            fixture.setFriction(0.3);
            fixture.setRestitution(0.0);
            fixture.setDensity(2.5);
            body.setBullet(true);
        }

        body.addFixture(fixture);
        body.setMass(MassType.NORMAL);

        double startX = Converter.toMeters(projectile.getPosition().getX());
        double startY = Converter.toMeters(projectile.getPosition().getY());
        body.translate(startX, startY);
        double angleRad = Math.toRadians(angleDegrees);

        Alien shooter = projectile.getShooter();
        boolean facingRight = shooter.getDirection();

        if (!facingRight) {
            angleRad = Math.PI - angleRad;
        }

        double baseVelocity = projectile.getWeapon().getBasePower();
        double velocityMagnitude =
                Math.max(baseVelocity, powerPercent * baseVelocity / 20);

        double vx = velocityMagnitude * Math.cos(angleRad);
        double vy = -velocityMagnitude * Math.sin(angleRad);

        body.setLinearVelocity(vx, vy);
        body.setUserData(projectile);

        projectile.setBody(body);
        world.addBody(body);
        activeProjectiles.add(projectile);
    }

    private void applyWindToProjectiles() {
        for (Projectile proj : activeProjectiles) {
            if (proj.isActive()) {
                Body body = proj.getBody();
                if (body != null) {
                    Vector2 windForce = wind.product(1); // 50% de la force du vent
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
     * <li>Synchronise la position du projectile logique avec celle du corps physique.</li>
     * <li>Vérifie si le projectile est sorti des limites de la carte (Out of Bounds).</li>
     * <li>Détecte si le projectile s'est arrêté (vitesse quasi nulle) pour déclencher son
     * explosion.</li>
     * <li>Nettoie la liste des projectiles actifs en retirant ceux qui sont morts
     * ou sortis.</li>
     * </ul>*
     * @param terrain  Le terrain de jeu actuel, utilisé pour vérifier les limites de la carte.
     */
    public void updateProjectiles(Terrain terrain) {

        applyWindToProjectiles();
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

            if (proj instanceof Throwable t) {
                t.decrementTimer();
                if (t.isReadyToExplode()) {
                    proj.setState(Projectile.State.EXPLODED);
                }
            }
            int mapWidth = terrain.getWidth();
            int mapHeight = terrain.getHeight();

            if (y > mapHeight + 100 || x < -500 || x > mapWidth + 500) {
                proj.setState(Projectile.State.OUT_OF_BOUNDS);
                world.removeBody(body);
                iterator.remove();
                continue;
            }
            if (proj.getState() == Projectile.State.EXPLODED) {
                explodeProjectile(proj);
                world.removeBody(body);
                iterator.remove();
                continue;
            }
            if (proj instanceof Throwable || proj instanceof Trap) {
                double triggerDistancePixels = 20.0;

                if (proj instanceof Trap) {
                    triggerDistancePixels = ((Trap) proj).getActivationRadius();
                }
                boolean exploded = false;
                for (Alien alien : physicsEngine.getAlienBodies().keySet()) {
                    if (!alien.isAlive()) {
                        continue;
                    }

                    if (alien == proj.getShooter() && proj.getFrameCount() < 60) {
                        continue;
                    }

                    Position alienPos = alien.getPosition();
                    double dx = alienPos.getX() - x;
                    double dy = alienPos.getY() - y;
                    double distSq = dx * dx + dy * dy;
                    if (distSq < triggerDistancePixels * triggerDistancePixels) {
                        proj.setState(Projectile.State.EXPLODED);
                        explodeProjectile(proj);
                        world.removeBody(body);
                        iterator.remove();
                        exploded = true;
                        break;
                    }
                }
                if (exploded) {
                    continue;
                }
            }

        }
    }


    /**
     * Gère la logique d'explosion d'un projectile.
     * <p>
     * Cette méthode applique les conséquences de l'explosion :
     * <ul>
     * <li>Calcule les dégâts infligés aux Aliens en fonction de leur distance
     * à l'impact.</li>
     * <li>Applique une force de recul (knockback) physique aux Aliens touchés.</li>
     * <li>(Note : La destruction du terrain est généralement gérée ailleurs
     * ou pourrait être ajoutée ici).</li>
     * </ul>*
     * @param proj Le projectile qui vient d'exploser.
     */
    public void explodeProjectile(Projectile proj) {
        if (proj instanceof Bullet) {
            return;
        }
        int explosionX = proj.getPosition().getX();
        int explosionY = proj.getPosition().getY();
        int radius = proj.getExplosionRadius();
        physicsEngine.getGameWorld().explode(explosionX, explosionY, radius);

        for (Alien alien : gameWorld.aliensInRadius(explosionX, explosionY, radius)) {
            Position alienPos = alien.getPosition();
            double distance = alienPos.distance(explosionX, explosionY);

            double ratio = 1.0 - (distance / radius);
            double knockbackForce = 50.0 * ratio;

            double dx = (alienPos.getX() - explosionX) / distance;
            double dy = (alienPos.getY() - explosionY) / distance;

            Body alienBody = physicsEngine.getAlienBodies().get(alien);
            if (alienBody != null) {
                alienBody.applyImpulse(
                        new Vector2(dx * knockbackForce, dy * knockbackForce)
                );
            }
        }
    }


    public void checkProjectileCollision(Body b1, Body b2) {
        if (b1 != null && b1.getUserData() instanceof Projectile p) {

            if (b2 != null && b2.getUserData() instanceof Alien alien) {
                if (p.getShooter() == alien && p.getFrameCount() < 30) {
                    return;
                }
            }
            if (!p.isActive()) {
                return;
            }
            if (p instanceof Rocket && b2 != null) {
                p.setState(Projectile.State.EXPLODED);
            } else if (p instanceof Bullet && b2 != null) {
                handleBulletImpact((Bullet) p, b2);
            }
        }
    }


    public void handleBulletImpact(Bullet bullet, Body target) {
        if (target == null || target.getUserData() instanceof Bullet) {
            return;
        }
        if (target.getUserData() instanceof Alien alien) {
            alien.receiveDmg(bullet.getDamage());
            target.applyImpulse(5);
        }
        bullet.setState(Projectile.State.EXPLODED);
    }

    public List<Projectile> getActiveProjectiles() {
        return activeProjectiles;
    }

    public void setWind(Vector2 wind) {
        this.wind = wind;
    }
}