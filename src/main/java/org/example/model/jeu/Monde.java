package org.example.model.jeu;

import org.example.model.entites.Worm;
import java.util.ArrayList;

public class Monde {
    private final Partie partie;
    private ArrayList<Worm> activeWorms;

    public Monde(Partie partie, Terrain terrain) {
        this.partie = partie;
        this.activeWorms = new ArrayList<>();
        for (Worm w : partie.getEquipe(1).getWorms()) {
            activeWorms.add(w);
        }
        for (Worm w : partie.getEquipe(2).getWorms()) {
            activeWorms.add(w);
        }
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

    public Terrain getTerrain() {
        return partie.getTerrain();
    }

}
