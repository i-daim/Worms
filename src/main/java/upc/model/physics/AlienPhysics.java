package upc.model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Capsule;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;
import upc.assets.SoundManager;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.Terrain;
import upc.model.jeu.TerrainGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <b>AlienPhysics</b>
 * <p>
 * Cette classe est le chef d'orchestre de la physique pour tous les ennemis (Aliens).
 * Elle fait le lien entre les objets du jeu (les entités Alien) et le moteur physique (Dyn4j).
 * </p>
 * <p>
 * <b>Ses missions principales :</b>
 * <ul>
 * <li>Gérer les corps physiques (création, suppression).</li>
 * <li>Synchroniser la physique (mètres) avec l'affichage (pixels).</li>
 * <li>Gérer les états physiques : gravité, friction, sauts, chutes.</li>
 * <li>Calculer les dégâts de chute basés sur la vitesse d'impact.</li>
 * <li>Gérer les collisions avec le terrain (sol solide) et l'eau.</li>
 * </ul>
 * </p>
 */
public class AlienPhysics {

    // Le monde physique de Dyn4j où vivent les corps
    private final World<Body> world;
    // Référence au terrain pour savoir où sont les murs et le sol
    private Terrain terrain;

    /**
     * Petite classe interne pour stocker l'état physique spécifique d'un Alien.
     * On garde une ref au Body Dyn4j et on track si l'alien a sauté ou sa vitesse précédente
     * peut etre utilisé pour calculer l'impact d'une chute.
     */
    private static class AlienState {
        private final Body body;
        private boolean hasJumped = false;
        private double previousVelocityY = 0.0;

        AlienState(Body body) {
            this.body = body;
        }
    }

    // Map qui lie chaque entité "Alien" (logique jeu) à son "AlienState" (physique)
    private final Map<Alien, AlienState> alienStates = new HashMap<>();

    // --- Constantes de Physique ---

    // Vitesse à partir de laquelle on commence à se faire mal en tombant
    private static final double SAFE_FALL_SPEED = 17.5;
    // Vitesse max de chute (terminal velocity) pour éviter les bugs de collision
    private static final double MAX_FALL_SPEED = 35.0;
    // Facteur pour convertir la vitesse supérieure à la max fall speed en points de dégâts
    private static final double DMG_MULTIPLIER = 0.75;

    // Friction nulle quand on bouge pour avancer fluidement
    private static final double FRICTION_MOVING = 0.0;
    // Friction très forte quand on s'arrête pour ne pas que l'Alien glisse
    private static final double FRICTION_STATIC = 100.0;

    // Propriétés du corps physique
    private static final double DENSITY = 2;
    // Résistance à l'air
    private static final double LINEAR_DAMPING = 4.0;

    // Dimensions de la hitbox de l'alien (converties en mètres)
    private static final double ALIEN_WIDTH = Converter.toMeters(18);
    private static final double ALIEN_HEIGHT = Converter.toMeters(25);

    /**
     * Constructeur du gestionnaire de physique.
     * @param world : Le monde Dyn4j dans lequel ajouter les bodies.
     * @param terrain : Le terrain actuel pour vérifier les collisions.
     */
    public AlienPhysics(World<Body> world, Terrain terrain) {
        this.world = world;
        this.terrain = terrain;
    }

    /**
     * <b>Méthode principale de mise à jour (boucle de jeu).</b><br>
     * À appeler à chaque frame. Elle parcourt tous les aliens, met à jour leur physique,
     * vérifie s'ils touchent le sol, gère les transitions entre différents états (IDLE, JUMP)
     * et applique les dégâts ou les sons si nécessaire.
     */
    public void updateAliens() {
        // On itère sur chaque duo Alien,State
        for (Map.Entry<Alien, AlienState> entry : alienStates.entrySet()) {
            Alien alien = entry.getKey();
            AlienState state = entry.getValue();
            Body body = state.body;

            // Si l'alien est mort, on ne fais plus de calculs pour celui ci
            if (!alien.isAlive()) {
                continue;
            }

            // --- Détection du sol ---
            // On regarde si les pieds touchent un bloc solide
            boolean physicalContact = isOnGround(body);
            // On vérifie si on est en train de monter (vitesse Y négative = vers le haut)
            // C'est important pour ne pas considérer qu'on est "au sol" pile au moment où on est en train de sauter
            boolean movingUp = body.getLinearVelocity().y < -1.0;

            // On est vraiment au sol si on touche le bas et qu'on ne décolle pas
            boolean reallyOnGround = physicalContact && !movingUp;

            // --- Gestion des États ---
            if (reallyOnGround) {
                // Atterrissage confirmé
                state.hasJumped = false;

                // Si on était en l'air (JUMP ou JUMP_START), on repasse en IDLE
                if (alien.getState()
                        == Alien.State.JUMP || alien.getState() == Alien.State.JUMP_START) {
                    alien.setState(Alien.State.IDLE);
                }
            } else {
                // Gestion de l'air (Transition IDLE/WALK -> JUMP si on tombe)
                double fallSpeed = body.getLinearVelocity().y;
                if (fallSpeed > 1.0 && alien.getState() != Alien.State.JUMP_START) {
                    alien.setState(Alien.State.JUMP);
                }
            }

            // --- Application Physique ---
            // Ajuste la friction (glissant ou collant) selon si on bouge ou pas
            updateMovementPhysics(alien, body, reallyOnGround);

            // Vérifie si la chute était violente (dégâts)
            checkFallDamage(alien, state, reallyOnGround);
            // Ajuste la résistance à l'air qui est modifiée quand on est dans les airs
            checkAirFriction(body, reallyOnGround);

            // --- Synchronisation Visuelle ---
            // On récupère la pos du corps physique (mètres) et on update l'objet Alien (pixels)
            Vector2 v = body.getTransform().getTranslation();
            int pixelX = Converter.toPixels(v.x);
            int pixelY = Converter.toPixels(v.y);
            alien.setPosition(new Position(pixelX, pixelY));

            // --- Gestion de l'Eau ---
            int tileY = pixelY / TerrainGenerator.TILE_SIZE;

            // Si on touche l'eau, petit effet sonore "Splash"
            if (terrain.isWater(tileY) && alien.getHealthPoints() > 0) {
                SoundManager.playEffect("Splash");
            }
            // Si on dépasse le niveau de l'eau (noyade), l'alien meurt direct
            if (tileY >= Terrain.getWaterLevel()) {
                alien.receiveDmg(alien.getHealthPoints());
            }

            // On stocke la vitesse actuelle pour le calcul de dégâts au prochain tour
            state.previousVelocityY = body.getLinearVelocity().y;
        }
    }

    /**
     * Ajuste dynamiquement la friction et la gravité de l'alien.
     * <br>
     * - Si <b>IDLE au sol</b> : On "colle" l'alien (friction max, gravité 0) pour qu'il ne glisse pas sur les pentes.
     * <br>
     * - Sinon (marche/saut) : On libère la physique (friction 0, gravité normale).
     *
     * @param alien : L'entité alien
     * @param body : Son corps physique
     * @param isOnGround : Si il est au sol ou non
     */
    private void updateMovementPhysics(Alien alien, Body body, boolean isOnGround) {
        BodyFixture fixture = body.getFixture(0);

        if (isOnGround && alien.getState() == Alien.State.IDLE) {
            // Mode "bloqué" : on arrête tout mouvement minime
            body.setGravityScale(0.0);
            body.setLinearVelocity(0, 0);
            body.setAngularVelocity(0);
            fixture.setFriction(FRICTION_STATIC);
        } else {
            // Mode "Libéré"
            body.setGravityScale(1.0);
            // Réveille le body s'il dormait
            body.setAtRest(false);
            // Ça glisse (pas de frottement au sol)
            fixture.setFriction(FRICTION_MOVING);
        }
    }

    /**
     * Crée un corps physique pour un nouvel alien et l'ajoute au monde.
     * @param alien : L'objet Alien à implementer physiquement.
     */
    public void addAlienBody(Alien alien) {
        Body body = createAlienBody(alien);
        this.world.addBody(body);
        // On l'enregistre dans notre map pour le suivre
        this.alienStates.put(alien, new AlienState(body));
    }

    /**
     * Configure le Body Dyn4j : Forme, masse, position initiale.
     * @param alien : L'alien source
     * @return Le Body prêt à être ajouté au monde
     */
    private Body createAlienBody(Alien alien) {
        Body body = new Body();
        Capsule capsuleShape = Geometry.createCapsule(ALIEN_WIDTH, ALIEN_HEIGHT);
        BodyFixture fixture = new BodyFixture(capsuleShape);

        fixture.setFriction(FRICTION_STATIC);
        fixture.setRestitution(0.0);
        fixture.setDensity(DENSITY);
        body.addFixture(fixture);

        body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
        body.setLinearDamping(LINEAR_DAMPING);
        body.setAngularDamping(10.0);

        body.translate(
                Converter.toMeters(alien.getPosition().getX()),
                Converter.toMeters(alien.getPosition().getY())
        );
        body.setUserData(alien);
        return body;
    }

    public void removeAlien(Alien alien) {
        AlienState state = alienStates.remove(alien);
        if (state != null) {
            world.removeBody(state.body);
        }
    }

    public void clearAliens() {
        for (AlienState state : alienStates.values()) {
            world.removeBody(state.body);
        }
        alienStates.clear();
    }

    public void moveAlien(Alien alien, double direction) {
        AlienState state = alienStates.get(alien);
        if (state != null) {
            // Déverrouillage immédiat pour permettre le mouvement
            state.body.setGravityScale(1.0);
            state.body.setAtRest(false);
            if (state.body.getFixtureCount() > 0) {
                state.body.getFixture(0).setFriction(FRICTION_MOVING);
            }

            double speed = 3.0 * direction;
            double velY = state.body.getLinearVelocity().y;
            state.body.setLinearVelocity(new Vector2(speed, velY));
        }
    }

    public void jumpAlien(Alien alien) {
        AlienState state = alienStates.get(alien);
        if (state != null && !state.hasJumped) {
            // Déverrouillage complet pour le saut
            state.body.setGravityScale(1.0);
            state.body.setAtRest(false);
            if (state.body.getFixtureCount() > 0) {
                state.body.getFixture(0).setFriction(FRICTION_MOVING);
            }

            Vector2 vel = state.body.getLinearVelocity();
            state.body.setLinearVelocity(vel.x, -20.0);
            state.hasJumped = true;
        }
    }

    public void flyAlien(Alien alien) {
        AlienState state = alienStates.get(alien);
        if (state != null) {
            state.body.setGravityScale(1.0);
            state.body.setAtRest(false);
            if (state.body.getFixtureCount() > 0) {
                state.body.getFixture(0).setFriction(FRICTION_MOVING);
            }

            Vector2 vel = state.body.getLinearVelocity();
            state.body.setLinearVelocity(vel.x, -10.0);
            state.hasJumped = true;
        }
    }

    private void checkAirFriction(Body body, boolean isOnGround) {
        body.setLinearDamping(isOnGround ? LINEAR_DAMPING : 0.5);
    }

    private void checkFallDamage(Alien alien, AlienState state, boolean isOnGround) {
        if (isOnGround) {
            double impactSpeed = state.previousVelocityY;
            if (impactSpeed > SAFE_FALL_SPEED) {
                int damage = (int) ((impactSpeed - SAFE_FALL_SPEED) * DMG_MULTIPLIER);
                if (damage > 0) {
                    alien.receiveDmg(damage);
                }
            }
        }
    }

    private boolean isOnGround(Body body) {
        Vector2 pos = body.getTransform().getTranslation();
        double x = pos.x;
        double feetY = pos.y + (ALIEN_HEIGHT / 2) + Converter.toMeters(2);
        double halfWidth = ALIEN_WIDTH / 2;

        return isSolid(x, feetY)
                || isSolid(x - halfWidth + Converter.toMeters(1), feetY)
                || isSolid(x + halfWidth - Converter.toMeters(1), feetY);
    }

    private boolean isSolid(double x, double y) {
        int tileX = Converter.toPixels(x) / TerrainGenerator.TILE_SIZE;
        int tileY = Converter.toPixels(y) / TerrainGenerator.TILE_SIZE;
        boolean[][] collisions = terrain.getCollisions();

        if (tileY >= 0 && tileY < collisions.length
                && tileX >= 0 && tileX < collisions[0].length) {
            return collisions[tileY][tileX];
        }
        return false;
    }

    public void limitFallVelocities() {
        for (AlienState state : alienStates.values()) {
            Vector2 vel = state.body.getLinearVelocity();
            if (vel.y > MAX_FALL_SPEED) {
                state.body.setLinearVelocity(vel.x, MAX_FALL_SPEED);
            }
        }
    }

    public Map<Alien, Body> getAlienBodies() {
        Map<Alien, Body> bodies = new HashMap<>();
        for (Map.Entry<Alien, AlienState> entry : alienStates.entrySet()) {
            bodies.put(entry.getKey(), entry.getValue().body);
        }
        return bodies;
    }

    public Set<Alien> getAliens() {
        return alienStates.keySet();
    }

    public Body getAlienBody(Alien alien) {
        AlienState state = alienStates.get(alien);
        return state != null ? state.body : null;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
}
