package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Terrain;
import org.dyn4j.world.listener.ContactListenerAdapter;
import org.dyn4j.world.ContactCollisionData;
import org.dyn4j.dynamics.contact.Contact;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <b>PhysicsEngine</b>
 * <p>
 * C'est le moteur central de la simulation physique du jeu.
 * Il agit comme le moteur qui synchronise le reste.
 * </p>
 * <p>
 * <b>Ses responsabilités :</b>
 * <ul>
 * <li>Posséder et mettre à jour le {@link World} de Dyn4j (le cœur de la simulation).</li>
 * <li>Gérer les constantes globales : Gravité, Vent, Temps (Delta Time).</li>
 * <li>Détecter les collisions (via le ContactListener) pour savoir quand un projectile touche quelque chose.</li>
 * <li>Déléguer la gestion spécifique aux sous-modules : {@link AlienPhysics}, {@link ProjectilePhysics}, {@link TerrainPhysics}.</li>
 * </ul>
 * </p>
 */
public class PhysicsEngine {

    // Références au monde du jeu et au terrain
    private final GameWorld gameWorld;
    private Terrain terrain;

    // Le monde physique (Dyn4j) qui contient tous les corps
    private final World<Body> world;

    // Sous-systèmes spécialisés pour éviter d'avoir une classe avec trop de lignes
    private final TerrainPhysics terrainPhysics;
    private final AlienPhysics alienPhysics;
    private final ProjectilePhysics projectilePhysics;

    // --- Paramètres de simulation ---
    private Vector2 wind = new Vector2(0, 0);
    private final double dt = 1.0 / 60.0;
    private final Vector2 gravity = new Vector2(0, 60.0);

    // Utilitaires
    private final Random random = new Random();
    private static final double MAX_WIND_POWER = 15.0;

    /**
     * Constructeur du moteur physique.
     * Initialise Dyn4j, configure la gravité et met en place l'écouteur
     * (listener) de collisions
     *
     * @param gameWorld : Référence au monde de jeu global.
     */
    public PhysicsEngine(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.terrain = gameWorld.getTerrain();

        // Création du monde Dyn4j
        this.world = new World<>();
        this.world.setGravity(gravity);

        // --- Gestion des Collisions ---
        // On ajoute un listener qui se déclenche dès que deux objets se touchent
        this.world.addContactListener(new ContactListenerAdapter<Body>() {
            @Override
            public void begin(ContactCollisionData<Body> collision, Contact contact) {
                // On récupère les deux corps impliqués dans le choc
                Body body1 = collision.getBody1();
                Body body2 = collision.getBody2();

                // On vérifie si l'un des deux est un projectile qui doit exploser
                checkProjectileCollision(body1, body2);
                checkProjectileCollision(body2, body1);
            }
        });

        this.alienPhysics = new AlienPhysics(world, terrain);
        // ProjectilePhysics a besoin d'une ref au moteur complet pour lire le vent
        this.projectilePhysics = new ProjectilePhysics(this);
        this.terrainPhysics = new TerrainPhysics(world, terrain);

        // Construction initiale des murs du terrain
        terrainPhysics.rebuildTerrain();
    }


////    UPDATE DES PHYSIQUES    ////

    /**
     * <b>Boucle principale de la physique.</b><br>
     * À appeler à chaque frame du jeu.
     * Elle fait avancer le temps et met à jour tous les sous-systèmes.
     */
    public void update() {
        world.update(dt);
        updateAliens();
        updateProjectiles();
        updateTerrain();
    }

    public void updateAliens() {
        alienPhysics.updateAliens();
        alienPhysics.limitFallVelocities();
    }

    public void updateProjectiles() {
        projectilePhysics.updateProjectiles(terrain);
    }

    public void updateTerrain() {
        terrainPhysics.updateTerrain();
    }


////    FONCTIONS POUR ALIENS    ////
    public void addAlien(Alien alien) {
        alienPhysics.addAlienBody(alien);
    }

////    FONCTIONS POUR PROJECTILES    ////
    public void addProjectile(Projectile projectile, int angleDegrees, int powerPercent) {
        projectilePhysics.addProjectile(projectile, angleDegrees, powerPercent);
    }

    private void checkProjectileCollision(Body b1, Body b2) {
        projectilePhysics.checkProjectileCollision(b1, b2);
    }


    public void randomizeWind() {
        // Calcul du facteur entre 0.1 et 1.0
        double powerFactor = 0.1 + (random.nextDouble() * 0.75);
        double direction = random.nextBoolean() ? 1.0 : -1.0;

        double windX = MAX_WIND_POWER * powerFactor * direction;
        double windY = (random.nextDouble() * 2.0) - 1.0;

        Vector2 windVector = new Vector2(windX, windY);

        // Application directe à la physique des projectiles
        this.projectilePhysics.setWind(windVector);

        // Met à jour la variable locale aussi si besoin pour affichage
        this.wind = windVector;
    }


////    GETTERS & SETTERS    ////
    public World<Body> getWorld() {
        return world;
    }

    public Map<Alien, Body> getAlienBodies() {
        return alienPhysics.getAlienBodies();
    }

    public List<Projectile> getActiveProjectiles() {
        return projectilePhysics.getActiveProjectiles();
    }

    public Body getAlienBody(Alien alien) {
        return alienPhysics.getAlienBody(alien);
    }

    public Vector2 getWind() {
        return wind;
    }

    public void setWind(double windX, double windY) {
        Vector2 v = new Vector2(windX, windY);
        this.wind = v;
        this.projectilePhysics.setWind(v);
    }

    public TerrainPhysics getTerrainPhysics() {
        return terrainPhysics;
    }

    public AlienPhysics getAlienPhysics() {
        return alienPhysics;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
        this.alienPhysics.setTerrain(terrain);
        this.terrainPhysics.setTerrain(terrain);
    }

    public void clearAliens() {
        alienPhysics.clearAliens();
    }

    public ProjectilePhysics getProjectilePhysics() {
        return projectilePhysics;
    }
}