package org.example.model.jeu;


import org.example.model.entites.Alien;
import org.example.model.entites.Position;
import org.example.model.items.Inventory;

import java.util.List;

public class Team {
    private final String name;
    private final Alien[] aliens;
    private int activeWormIndex;
    private final Inventory inventory = new Inventory();


    public Team(String name, int teamSize) {
        this.name = name;
        this.aliens = new Alien[teamSize];
        this.activeWormIndex = 0;
    }

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Alien[] getWorms() {
        return aliens;
    }

    public int getActiveWormIndex() {
        return activeWormIndex;
    }

    public Alien getActiveWorm() {
        if (activeWormIndex < aliens.length && aliens[activeWormIndex] != null) {
            return aliens[activeWormIndex];
        }
        return null;
    }

    public void initializeTeam(String mapName, int team) {
        List<Position> spawns = SpawnPoints.getSpawns(mapName, team);
        for (int i = 0; i < aliens.length; i++) {
            aliens[i] = new Alien(this, spawns.get(i));
        }
    }

    public void skipToNextWorm() {
        int attempts = 0;
        do {
            activeWormIndex = (activeWormIndex + 1) % aliens.length;
            attempts++;
        } while (attempts < aliens.length
                &&
                (aliens[activeWormIndex] == null || !aliens[activeWormIndex].isAlive()));
    }

    public boolean isEliminated() {
        for (Alien w : aliens) {
            if (w != null && w.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public int getAliveWormsNumber() {
        int count = 0;
        for (Alien w : aliens) {
            if (w != null && w.isAlive()) {
                count++;
            }
        }
        return count;
    }

    public void resetIndex() {
        activeWormIndex = 0;

        // Trouver le premier worm vivant
        while (activeWormIndex < aliens.length
                && (aliens[activeWormIndex] == null || !aliens[activeWormIndex].isAlive())) {
            activeWormIndex++;
        }
    }

    public boolean isActiveLastWorm() {
        for (int i = activeWormIndex + 1; i < aliens.length; i++) {
            if (aliens[i] != null && aliens[i].isAlive()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public String toString() {
        return name + " (" + getAliveWormsNumber() + "/" + aliens.length + " vivants)";
    }


}
