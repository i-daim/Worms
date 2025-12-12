package upc.model.jeu;


import upc.model.entites.Alien;
import upc.model.entites.Position;

import java.util.List;

/**
 * <p>Représente une équipe de joueurs (Aliens) dans le jeu.</p>
 *
 * <p>Cette classe gère un ensemble d'entités {@link Alien}, assure le suivi de l'alien
 * actif qui est en train de jouer, et contient l'inventaire commun de l'équipe.</p>
 */
public class Team {
    /** Le nom de l'équipe. */
    private final String name;
    /** Le tableau des entités {@link Alien} appartenant à cette équipe. */
    private final Alien[] aliens;
    /** L'indice dans le tableau {@code aliens} de l'Alien actuellement actif. */
    private int activeAlienIndex;
    /** L'inventaire de l'équipe, contenant les armes et outils disponibles. */
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

    public void initializeTeam(GameWorld gameWorld, String mapName, int team) {
        List<Position> spawns = SpawnPoints.getSpawns(mapName, team);
        for (int i = 0; i < aliens.length; i++) {
            aliens[i] = new Alien(gameWorld, this, spawns.get(i));
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
