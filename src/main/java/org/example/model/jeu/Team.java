package org.example.model.jeu;


import org.example.model.entites.Alien;
import org.example.model.entites.Position;
import org.example.model.items.Inventory;

import java.util.List;

public class Team {
    private final String nom;
    private final Alien[] aliens;
    private int indexWormActif;
    private final Inventory inventory = new Inventory();


    public Team(String nom, int tailleEquipe) {
        this.nom = nom;
        this.aliens = new Alien[tailleEquipe];
        this.indexWormActif = 0;
    }

    public String getNom() {
        return nom;
    }

    public Inventory getInventaire() {
        return inventory;
    }

    public Alien[] getWorms() {
        return aliens;
    }

    public int getIndexWormActif() {
        return indexWormActif;
    }

    public Alien getWormActif() {
        if (indexWormActif < aliens.length && aliens[indexWormActif] != null) {
            return aliens[indexWormActif];
        }
        return null;
    }

    public void initialiserEquipe(String mapName, int team) {
        List<Position> spawns = SpawnPoints.getSpawns(mapName, team);
        for (int i = 0; i < aliens.length; i++) {
            aliens[i] = new Alien(this, spawns.get(i));
        }
    }

    public void passerAuWormSuivant() {
        int tentatives = 0;
        do {
            indexWormActif = (indexWormActif + 1) % aliens.length;
            tentatives++;
        } while (tentatives < aliens.length
                &&
                (aliens[indexWormActif] == null || !aliens[indexWormActif].isAlive()));
    }

    public boolean estEliminee() {
        for (Alien w : aliens) {
            if (w != null && w.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public int getNombreWormsVivants() {
        int count = 0;
        for (Alien w : aliens) {
            if (w != null && w.isAlive()) {
                count++;
            }
        }
        return count;
    }

    public void resetIndex() {
        indexWormActif = 0;

        // Trouver le premier worm vivant
        while (indexWormActif < aliens.length
                && (aliens[indexWormActif] == null || !aliens[indexWormActif].isAlive())) {
            indexWormActif++;
        }
    }

    public boolean isActiveLastWorm() {
        for (int i = indexWormActif + 1; i < aliens.length; i++) {
            if (aliens[i] != null && aliens[i].isAlive()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public String toString() {
        return nom + " (" + getNombreWormsVivants() + "/" + aliens.length + " vivants)";
    }


}
