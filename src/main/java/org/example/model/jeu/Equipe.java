package org.example.model.jeu;


import org.example.model.entites.Worm;
import org.example.model.items.Inventaire;
import java.util.Random;

public class Equipe {
    private final String nom;
    private final Worm[] worms;
    private int indexWormActif;
    private final Inventaire inventaire = new Inventaire();


    public Equipe(String nom, int tailleEquipe) {
        this.nom = nom;
        this.worms = new Worm[tailleEquipe];
        this.indexWormActif = 0;
    }

    public String getNom() {
        return nom;
    }

    public Inventaire getInventaire() {
        return inventaire;
    }

    public Worm[] getWorms() {
        return worms;
    }

    public int getIndexWormActif() {
        return indexWormActif;
    }

    public Worm getWormActif() {
        if (indexWormActif < worms.length && worms[indexWormActif] != null) {
            return worms[indexWormActif];
        }
        return null;
    }

    public void initialiserEquipe(int zoneX, int zoneY, int largeurZone, int hauteurZone) {
        Random random = new Random();

        for (int i = 0; i < worms.length; i++) {
            int x = zoneX + random.nextInt(largeurZone);
            int y = zoneY + random.nextInt(hauteurZone);

            worms[i] = new Worm(this, x, y);
        }
    }
    public void passerAuWormSuivant() {
        int tentatives = 0;
        do {
            indexWormActif = (indexWormActif + 1) % worms.length;
            tentatives++;
        } while (tentatives < worms.length
                &&
                (worms[indexWormActif] == null || !worms[indexWormActif].isAlive()));
    }

    public boolean estEliminee() {
        for (Worm w : worms) {
            if (w != null && w.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public int getNombreWormsVivants() {
        int count = 0;
        for (Worm w : worms) {
            if (w != null && w.isAlive()) {
                count++;
            }
        }
        return count;
    }

    public void resetIndex() {
        indexWormActif = 0;

        // Trouver le premier worm vivant
        while (indexWormActif < worms.length
                && (worms[indexWormActif] == null || !worms[indexWormActif].isAlive())) {
            indexWormActif++;
        }
    }


    @Override
    public String toString() {
        return nom + " (" + getNombreWormsVivants() + "/" + worms.length + " vivants)";
    }


}
