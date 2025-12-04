package upc.model.jeu;

import upc.model.entites.Alien;
import upc.model.entites.Projectile;
import upc.model.entites.Trap;
import upc.model.physics.PhysicsEngine;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private final GameManager gameManager;
    private final PhysicsEngine physicsEngine;

    private final ArrayList<Alien> activeAliens;
    private final ArrayList<Trap> activeTraps;

    public GameWorld(GameManager gameManager, Terrain terrain) {
        this.gameManager = gameManager;
        this.physicsEngine = new PhysicsEngine(terrain);

        this.activeAliens = new ArrayList<>();
        this.activeTraps = new ArrayList<>();


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

    public Terrain getTerrain() {
        return gameManager.getTerrain();
    }

    public ArrayList<Alien> getActiveWorms() {
        ArrayList<Alien> living = new ArrayList<>();
        for (Alien a : activeAliens) {
            if (a != null && a.isAlive()) {
                living.add(a);
            }
        }
        return living;
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

    public void addTrap(Trap trap) {
        activeTraps.add(trap);
    }
    public void removeTrap(Trap trap) {
        activeTraps.remove(trap);
    }


    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;

    }
    public List<Projectile> getActiveProjectiles() {
        return physicsEngine.getActiveProjectiles();
    }

    public ArrayList<Trap> getActiveTraps() {
        return activeTraps;
    }
}
