package org.example.model.jeu;


import org.example.model.entites.Alien;
import org.example.model.entites.Position;
import org.example.model.items.Inventory;

import java.util.List;

public class Team {
    private final String name;
    private final Alien[] aliens;
    private int activeAlienIndex;
    private final Inventory inventory = new Inventory();


    public Team(String name, int teamSize) {
        this.name = name;
        this.aliens = new Alien[teamSize];
        this.activeAlienIndex = 0;
    }

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Alien[] getAliens() {
        return aliens;
    }

    public int getActiveAlienIndex() {
        return activeAlienIndex;
    }

    public Alien getActiveAlien() {
        if (activeAlienIndex < aliens.length && aliens[activeAlienIndex] != null) {
            return aliens[activeAlienIndex];
        }
        return null;
    }

    public void initializeTeam(String mapName, int team) {
        List<Position> spawns = SpawnPoints.getSpawns(mapName, team);
        for (int i = 0; i < aliens.length; i++) {
            aliens[i] = new Alien(this, spawns.get(i));
        }
    }

    public void skipToNextAlien() {
        int attempts = 0;
        do {
            activeAlienIndex = (activeAlienIndex + 1) % aliens.length;
            attempts++;
        } while (attempts < aliens.length
                &&
                (aliens[activeAlienIndex] == null || !aliens[activeAlienIndex].isAlive()));
    }

    public boolean isEliminated() {
        for (Alien a : aliens) {
            if (a != null && a.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public int getAliveAliensNumber() {
        int count = 0;
        for (Alien a : aliens) {
            if (a != null && a.isAlive()) {
                count++;
            }
        }
        return count;
    }

    public void resetIndex() {
        activeAlienIndex = 0;

        // Trouver le premier alien vivant
        while (activeAlienIndex < aliens.length
                && (aliens[activeAlienIndex] == null || !aliens[activeAlienIndex].isAlive())) {
            activeAlienIndex++;
        }
    }

    public boolean isActiveLastAlien() {
        for (int i = activeAlienIndex + 1; i < aliens.length; i++) {
            if (aliens[i] != null && aliens[i].isAlive()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public String toString() {
        return name + " (" + getAliveAliensNumber() + "/" + aliens.length + " vivants)";
    }


}
