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
    public void explode(int x, int y, int radius) {
        getTerrain().makeHole(x, y, radius);
        getPhysicsEngine().getTerrainPhysics().setModified(true);
        gameManager.addActiveAnimation("explosion", x, y);
    }


    public Terrain getTerrain() {
        return gameManager.getTerrain();
    }

    public ArrayList<Alien> getActiveAliens() {
        ArrayList<Alien> living = new ArrayList<>();
        for (Alien a : activeAliens) {
            if (a != null && a.isAlive()) {
                living.add(a);
            }
        }
        return living;
    }

    public void initializePhysics() {
        physicsEngine.clearAliens();
        activeAliens.clear();

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

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;

    }
    public List<Projectile> getActiveProjectiles() {
        return physicsEngine.getActiveProjectiles();
    }
    public GameManager getGameManager() {
        return gameManager;
    }
}
