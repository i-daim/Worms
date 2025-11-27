package org.example.model.jeu;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;

public class Partie {
    public static final int ROUND_TIME = 60;
    public static final int TEAM_SIZE = 2;

    private final Terrain terrain;
    private final Monde monde;
    private final Equipe[] equipes;

    private int activeTeamIndex;
    private int activeWormIndex;
    private int currentRound;
    private int timeLeft;
    private boolean gameOver;
    private boolean actionDone;

    public Partie(String mapName) {
        this.terrain = TerrainGenerator.creerTerrain(mapName);
        this.monde = new Monde(this, this.terrain);

        this.equipes = new Equipe[2];
        this.equipes[0] = new Equipe("Équipe 1", TEAM_SIZE);
        this.equipes[1] = new Equipe("Équipe 2", TEAM_SIZE);

        initializeGame();
    }

    public void update(double elapsedTime) {
        monde.update(elapsedTime);

        checkGameOver();

        countTimeLeft();
    }

    private void initializeGame() {
        equipes[0].initialiserEquipe(5, -100, 8, 1);
        equipes[1].initialiserEquipe(35, -100, 8, 1);

        monde.initialiserPhysique();

        this.activeTeamIndex = 0;
        this.activeWormIndex = 0;
        this.currentRound = 1;
        this.timeLeft = ROUND_TIME;
        this.gameOver = false;
        this.actionDone = false;
    }


    public void skipTurn() {
        actionDone = false;

        if (getTeam(0).isActiveLastWorm() && getTeam(1).isActiveLastWorm()) {
            currentRound++;
        }

        nextTurn();

        timeLeft = ROUND_TIME;
    }

    private void nextTurn() {
        activeTeamIndex = (activeTeamIndex + 1) % equipes.length;

        if (getActiveTeam().estEliminee()) {
            gameOver = true;
            return;
        }
        getActiveTeam().passerAuWormSuivant();

        activeWormIndex = getActiveTeam().getIndexWormActif();
    }

    public void countTimeLeft() {
        if (timeLeft > 0) {
            timeLeft--;
        } else {
            skipTurn();
        }
    }

    private void checkGameOver() {
        for (Equipe equipe : equipes) {
            if (equipe.estEliminee()) {
                gameOver = true;
                return;
            }
        }
    }

    public Worm attaquerPosition(int x, int y, int degats) {
        if (actionDone || degats < 0) {
            return null;
        }

        Worm cible = trouverWormAPosition(x, y);
        if (cible != null) {
            cible.receiveDmg(degats);
            checkGameOver();
            actionDone = true;
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


    /* GETTERS */

    public Terrain getTerrain() {
        return terrain;
    }

    public Equipe getActiveTeam() {
        return equipes[activeTeamIndex];
    }

    public Equipe getEnnemyTeam() {
        int indexEnnemi = (activeTeamIndex + 1) % equipes.length;
        return equipes[indexEnnemi];
    }

    public Equipe getTeam(int index) {
        if (index >= 0 && index < equipes.length) {
            return equipes[index];
        }
        return null;
    }

    public Equipe[] getTeams() {
        return equipes;
    }

    public int getActiveTeamIndex() {
        return activeTeamIndex;
    }

    public Worm getActiveWorm() {
        return getActiveTeam().getWormActif();
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public boolean isOver() {
        return gameOver;
    }

    public Equipe getWinningTeam() {
        if (!gameOver) {
            return null;
        }

        for (Equipe equipe : equipes) {
            if (!equipe.estEliminee()) {
                return equipe;
            }
        }

        return null;
    }

    public int getAliveWorms() {
        int total = 0;
        for (Equipe equipe : equipes) {
            total += equipe.getNombreWormsVivants();
        }
        return total;
    }

    public boolean isActionDone() {
        return actionDone;
    }

    public Monde getMonde() {
        return monde;
    }

    @Override
    public String toString() {
        return String.format("Tour %d | %s | Temps: %ds",
                currentRound, getActiveTeam().getNom(), timeLeft);
    }
}
