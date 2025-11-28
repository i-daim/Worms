package org.example.model.jeu;

import org.example.model.entites.Alien;
import org.example.model.entites.Projectile;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private final GameManager gameManager;
    private final PhysicsEngine physicsEngine;
    private ArrayList<Alien> activeAliens;

    public GameWorld(GameManager gameManager, Terrain terrain) {
        this.gameManager = gameManager;
        this.physicsEngine = new PhysicsEngine(terrain);
        this.activeAliens = new ArrayList<>();

        initialiserPhysique();
    }

    public void update(double elapsedTime) {
        physicsEngine.update();

    }

    public ArrayList<Alien> wormsDansRayon(int x, int y, int rayon) {
        ArrayList<Alien> aliens = new ArrayList<>();
        for (Alien w : this.activeAliens) {
            if (w.getPosition().distance(x, y) <= rayon) {
                aliens.add(w);
            }
        }
        return aliens;
    }

    public ArrayList<Alien> getActiveWorms() {
        ArrayList<Alien> living = new ArrayList<>();
        for (Alien w : activeAliens) {
            if (w != null && w.isAlive()) {
                living.add(w);
            }
        }
        return living;
    }

    public void initialiserPhysique() {
        for (Alien w : gameManager.getTeam(0).getWorms()) {
            if (w != null) {
                activeAliens.add(w);
                physicsEngine.addWorm(w);
            }
        }
        for (Alien w : gameManager.getTeam(1).getWorms()) {
            if (w != null) {
                activeAliens.add(w);
                physicsEngine.addWorm(w);
            }
        }
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;

    }
    public List<Projectile> getActiveProjectiles() {
        return physicsEngine.getActiveProjectiles();
    }

}
