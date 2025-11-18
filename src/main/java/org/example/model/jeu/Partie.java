package org.example.model.jeu;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;

public class Partie {
    private static final int TEMPS_DE_JEU = 60;
    private static final int NOMBRE_WORMS_PAR_EQUIPE = 2;

    private final Terrain terrain;
    private final Equipe[] equipes;

    private int indexEquipeActive;
    private int tourActuel;
    private int tempsRestant;
    private boolean partieTerminee;
    private boolean actionEffectuee;

    public Partie(String mapName) {
        this.terrain = TerrainGenerator.creerTerrain(mapName);
        this.equipes = new Equipe[2];
        this.equipes[0] = new Equipe("Équipe 1", NOMBRE_WORMS_PAR_EQUIPE);
        this.equipes[1] = new Equipe("Équipe 2", NOMBRE_WORMS_PAR_EQUIPE);
        this.indexEquipeActive = 0;
        this.tourActuel = 1;
        this.tempsRestant = TEMPS_DE_JEU;
        this.partieTerminee = false;
        this.actionEffectuee = false;

        initialiserPartie();
    }

    private void initialiserPartie() {
        equipes[0].initialiserEquipe(5, 5, 8, 8);
        equipes[1].initialiserEquipe(35, 5, 8, 8);
    }

    public boolean deplacerWormActuelGauche() {
        Worm worm = getWormActif();
        if (worm != null && worm.isAlive() && !actionEffectuee) {
            boolean succes = worm.moveLeft(terrain);
            if (succes) {
                actionEffectuee = true;
            }
            return succes;
        }
        return false;
    }

    public boolean deplacerWormActuelDroite() {
        Worm worm = getWormActif();
        if (worm != null && worm.isAlive() && !actionEffectuee) {
            boolean succes = worm.moveRight(terrain);
            if (succes) {
                actionEffectuee = true;
            }
            return succes;
        }
        return false;
    }

    public boolean sauterWormActuel() {
        Worm worm = getWormActif();
        if (worm != null && worm.isAlive() && !actionEffectuee) {
            boolean succes = worm.jump(terrain);
            if (succes) {
                actionEffectuee = true;
            }
            return succes;
        }
        return false;
    }

    public Worm attaquerPosition(int x, int y, int degats) {
        if (actionEffectuee || degats < 0) {
            return null;
        }

        Worm cible = trouverWormAPosition(x, y);
        if (cible != null) {
            cible.receiveDmg(degats);
            verifierFinPartie();
            actionEffectuee = true;
        }
        return cible;
    }

    private Worm trouverWormAPosition(int x, int y) {
        for (Equipe equipe : equipes) {
            for (Worm worm : equipe.getWorms()) {
                if (worm != null && worm.isAlive()) {
                    Position pos = worm.getPosition();
                    if (pos.getX() == x && pos.getY() == y) {
                        return worm;
                    }
                }
            }
        }
        return null;
    }

    public void passerTour() {
        actionEffectuee = false;

        getEquipeActive().passerAuWormSuivant();

        if (tousLesWormsOntJoue()) {
            changerEquipe();
        }

        tempsRestant = TEMPS_DE_JEU;
    }

    private boolean tousLesWormsOntJoue() {
        Equipe equipe = getEquipeActive();
        int count = 0;

        for (Worm worm : equipe.getWorms()) {
            if (worm != null && worm.isAlive()) {
                count++;
            }
        }

        return count <= 1 || equipe.getIndexWormActif() == 0;
    }

    private void changerEquipe() {
        indexEquipeActive = (indexEquipeActive + 1) % equipes.length;

        if (indexEquipeActive == 0) {
            tourActuel++;
        }

        if (getEquipeActive().estEliminee()) {
            partieTerminee = true;
            return;
        }

        getEquipeActive().resetIndex();
    }

    public void decompterTemps() {
        if (tempsRestant > 0) {
            tempsRestant--;
        } else {
            passerTour();
        }
    }

    private void verifierFinPartie() {
        for (Equipe equipe : equipes) {
            if (equipe.estEliminee()) {
                partieTerminee = true;
                return;
            }
        }
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public Equipe getEquipeActive() {
        return equipes[indexEquipeActive];
    }

    public Equipe getEquipeEnnemie() {
        int indexEnnemi = (indexEquipeActive + 1) % equipes.length;
        return equipes[indexEnnemi];
    }

    public Equipe getEquipe(int index) {
        if (index >= 0 && index < equipes.length) {
            return equipes[index];
        }
        return null;
    }

    public Equipe[] getEquipes() {
        return equipes;
    }

    public int getIndexEquipeActive() {
        return indexEquipeActive;
    }

    public Worm getWormActif() {
        return getEquipeActive().getWormActif();
    }

    public int getTourActuel() {
        return tourActuel;
    }

    public int getTempsRestant() {
        return tempsRestant;
    }

    public boolean estTerminee() {
        return partieTerminee;
    }

    public Equipe getEquipeGagnante() {
        if (!partieTerminee) {
            return null;
        }

        for (Equipe equipe : equipes) {
            if (!equipe.estEliminee()) {
                return equipe;
            }
        }

        return null;
    }

    public int getNombreWormsVivants() {
        int total = 0;
        for (Equipe equipe : equipes) {
            total += equipe.getNombreWormsVivants();
        }
        return total;
    }

    public boolean actionDejaEffectuee() {
        return actionEffectuee;
    }

    @Override
    public String toString() {
        return String.format("Tour %d | %s | Temps: %ds",
                tourActuel, getEquipeActive().getNom(), tempsRestant);
    }
}
