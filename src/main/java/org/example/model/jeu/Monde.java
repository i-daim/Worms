package org.example.model.jeu;

import org.example.model.entites.Worm;
import java.util.ArrayList;

public class Monde {
    private final Partie partie;
    private final PhysicsEngine physicsEngine;
    private ArrayList<Worm> activeWorms;

    public Monde(Partie partie, Terrain terrain) {
        this.partie = partie;
        this.physicsEngine = new PhysicsEngine(terrain);
        this.activeWorms = new ArrayList<>();

        for (Worm w : partie.getTeam(0).getWorms()) {
            activeWorms.add(w);
            physicsEngine.addWorm(w);
        }
        for (Worm w : partie.getTeam(1).getWorms()) {
            activeWorms.add(w);
            physicsEngine.addWorm(w);
        }
    }

    public void update(double elapsedTime) {
        physicsEngine.update();

    }

    public ArrayList<Worm> wormsDansRayon(int x, int y, int rayon) {
        ArrayList<Worm> worms = new ArrayList<>();
        for (Worm w : this.activeWorms) {
            if (w.getPosition().distance(x, y) <= rayon) {
                worms.add(w);
            }
        }
        return worms;
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;

    }
}
