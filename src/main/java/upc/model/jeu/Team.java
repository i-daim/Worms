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


    /**
     * Constructeur pour {@code Team}.
     *
     * @param name Le nom de l'équipe.
     * @param teamSize Le nombre maximum d'Aliens dans l'équipe.
     */
    public Team(String name, int teamSize) {
        this.name = name;
        this.aliens = new Alien[teamSize];
        this.activeAlienIndex = 0;
    }

    /**
     * Retourne le nom de l'équipe.
     *
     * @return Le nom de l'équipe.
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne l'inventaire commun de l'équipe.
     *
     * @return L'objet {@link Inventory}.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Retourne le tableau des Aliens de l'équipe.
     *
     * @return Le tableau d'objets {@link Alien}.
     */
    public Alien[] getAliens() {
        return aliens;
    }

    /**
     * Retourne l'indice dans le tableau de l'Alien actuellement actif.
     *
     * @return L'index de l'Alien actif.
     */
    public int getActiveAlienIndex() {
        return activeAlienIndex;
    }


    /**
     * Retourne l'Alien actuellement actif (celui qui est en train de jouer).
     *
     * @return L'objet {@link Alien} actif, ou {@code null} si l'index est invalide ou l'Alien n'existe pas.
     */
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
