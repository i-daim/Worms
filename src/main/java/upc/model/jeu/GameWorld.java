package upc.model.jeu;

import upc.model.entites.Alien;
import upc.model.entites.projectiles.Projectile;
import upc.model.physics.PhysicsEngine;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private final GameManager gameManager;
    private final PhysicsEngine physicsEngine;

    private final ArrayList<Alien> activeAliens;

    public GameWorld(GameManager gameManager, Terrain terrain) {
        this.gameManager = gameManager;
        this.physicsEngine = new PhysicsEngine(this);

        this.activeAliens = new ArrayList<>();

        initializePhysics();
    }

    public void update(double elapsedTime) {
        physicsEngine.update();

    }

    public ArrayList<Alien> aliensInRadius(int x, int y, int radius) {
        ArrayList<Alien> aliens = new ArrayList<>();
        for (Alien a : this.activeAliens) {
            if (a.getPosition().distance(x, y) <= radius) {
                aliens.add(a);
            }
        }
        return aliens;
    }

    public void explode(int x, int y, Projectile projectile) {
        int radius = projectile.getExplosionRadius();
        projectile.applyExplosionDamage(this);

        getTerrain().makeHole(x, y, radius);
        getPhysicsEngine().getTerrainPhysics().setModified(true);
        gameManager.addActiveAnimation("explosion", x, y);
    }



    public void initializePhysics() {
        for (Alien a : gameManager.getTeam(0).getAliens()) {
            if (a != null) {
                activeAliens.add(a);
                physicsEngine.addAlien(a);
            }
        }
        for (Alien a : gameManager.getTeam(1).getAliens()) {
            if (a != null) {
                activeAliens.add(a);
                physicsEngine.addAlien(a);
            }
        }
    }

    /// Getter ///

    /**
     * Retourne l'ensemble des aliens encore en vie dans le jeu
     * @return une liste {@link ArrayList<Alien>}
     * représentant l'ensemble des aliens vivants
     */
    public ArrayList<Alien> getActiveAliens() {
        ArrayList<Alien> living = new ArrayList<>();
        for (Alien a : activeAliens) {
            if (a != null && a.isAlive()) {
                living.add(a);
            }
        }
        return living;
    }


    /**
     * Retourne le terrain de jeu
     * @return le terrain de jeu via l'attribut gameManager
     */
    public Terrain getTerrain() {
        return gameManager.getTerrain();
    }

    /**
     * Retourne la physique du jeu
     * @return l'attribut physicsEngine
     */
    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;

    }

    /**
     * Retourne l'ensemble des projectiles actuellement actives dans le jeu
     * @return l'ensemble des projectiles via l'attribut physicsEngine
     */
    public List<Projectile> getActiveProjectiles() {
        return physicsEngine.getActiveProjectiles();
    }

    /**
     * Retourne la classe gestionnaire du jeu
     * @return l'attribut gameManager
     * */
    public GameManager getGameManager() {
        return gameManager;
    }
}
