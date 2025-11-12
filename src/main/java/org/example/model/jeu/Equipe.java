package org.example.model.jeu;

import org.example.model.entites.Worm;

import java.util.Random;

public class Equipe {
    private final String nom;
    private final Worm[] worms;
    private int indexWormActif;

    public Equipe(String nom, int tailleEquipe) {
        this.nom = nom;
        this.worms = new Worm[tailleEquipe];
        this.indexWormActif = 0;
    }

    public String getNom() {
        return nom;
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
        } while (tentatives < worms.length &&
                (worms[indexWormActif] == null || !worms[indexWormActif].estVivant()));
    }

    public boolean estEliminee() {
        for (Worm w : worms) {
            if (w != null && w.estVivant()) {
                return false;
            }
        }
        return true;
    }


}
