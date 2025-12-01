package upc.model.jeu;

import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.entites.Projectile;
import upc.model.items.Weapon;

public class GameManager {
    public static final int ROUND_TIME = 60;
    public static final int TEAM_SIZE = 2;

    private final Terrain terrain;
    private final GameWorld gameWorld;
    private final Team[] teams;

    private int activeTeamIndex;
    private int currentRound;
    private int timeLeft;
    private boolean gameOver;
    private boolean actionDone;

    private String mapName;

    public GameManager(String mapName) {
        this.mapName = mapName;
        this.terrain = TerrainGenerator.createTerrain(mapName);

        this.teams = new Team[2];
        this.teams[0] = new Team("Zlerps", TEAM_SIZE);
        this.teams[1] = new Team("Glorbs", TEAM_SIZE);

        this.gameWorld = new GameWorld(this, this.terrain);

        initializeGame();
    }

    public void update(double elapsedTime) {
        //Vérifie que le vers controlé est toujours en vie sinon on passe à l'adversaire
        if (getActiveAlien() == null || !getActiveAlien().isAlive()) {
            skipTurn();
            return;
        }
        gameWorld.update(elapsedTime);

        checkGameOver();

        countTimeLeft();
    }

    private void initializeGame() {
        teams[0].initializeTeam(mapName, 0);
        teams[1].initializeTeam(mapName, 1);

        gameWorld.initializePhysics();

        this.activeTeamIndex = 0;
        this.currentRound = 1;
        this.timeLeft = ROUND_TIME;
        this.gameOver = false;
        this.actionDone = false;
    }


    public void skipTurn() {
        actionDone = false;

        if (getTeam(0).isActiveLastAlien() && getTeam(1).isActiveLastAlien()) {
            currentRound++;
        }

        nextTurn();

        timeLeft = ROUND_TIME;
    }

    private void nextTurn() {
        activeTeamIndex = (activeTeamIndex + 1) % teams.length;

        if (getActiveTeam().isEliminated()) {
            gameOver = true;
            return;
        }
        getActiveTeam().skipToNextAlien();
    }

    private int frameCounter = 0;

    public void countTimeLeft() {
        frameCounter++;
        if (frameCounter >= 60) {  // 1 seconde à 60 FPS
            frameCounter = 0;
            if (timeLeft > 0) {
                timeLeft--;
            } else {
                skipTurn();
            }
        }
    }


    private void checkGameOver() {
        for (Team team : teams) {
            if (team.isEliminated()) {
                gameOver = true;
                return;
            }
        }
    }

    public Alien attackPosition(int x, int y, int damage) {
        if (actionDone || damage < 0) {
            return null;
        }

        Alien target = getAlienAtPosition(x, y);
        if (target != null) {
            target.receiveDmg(damage);
            checkGameOver();
            actionDone = true;
        }
        return target;
    }

    public Alien getAlienAtPosition(int x, int y) {
        for (Team team : teams) {
            for (Alien alien : team.getAliens()) {
                if (alien != null && alien.isAlive()) {
                    Position pos = alien.getPosition();
                    if (pos.getX() == x && pos.getY() == y) {
                        return alien;
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

    public GameWorld getWorld() {
        return gameWorld;
    }

    public Team getActiveTeam() {
        return teams[activeTeamIndex];
    }

    public Team getEnnemyTeam() {
        int indexEnnemi = (activeTeamIndex + 1) % teams.length;
        return teams[indexEnnemi];
    }

    public Team getTeam(int index) {
        if (index >= 0 && index < teams.length) {
            return teams[index];
        }
        return null;
    }

    public Team[] getTeams() {
        return teams;
    }

    public int getActiveTeamIndex() {
        return activeTeamIndex;
    }

    public Alien getActiveAlien() {
        return getActiveTeam().getActiveAlien();
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

    public Team getWinningTeam() {
        if (!gameOver) {
            return null;
        }

        for (Team team : teams) {
            if (!team.isEliminated()) {
                return team;
            }
        }

        return null;
    }

    public int getAliveWorms() {
        int total = 0;
        for (Team team : teams) {
            total += team.getAliveAliensNumber();
        }
        return total;
    }

    public boolean isActionDone() {
        return actionDone;
    }

    public Projectile fireWeapon(Weapon weapon, int angleDegrees, int powerPercent) {
        if (actionDone || powerPercent < 0 || powerPercent > 100) {
            return null;
        }

        Alien shooter = getActiveAlien();
        if (shooter == null || !shooter.isAlive()) {
            return null;
        }

        Projectile proj = weapon.createProjectile(shooter);
        gameWorld.getPhysicsEngine().addProjectile(proj, angleDegrees, powerPercent);

        actionDone = true;
        return proj;
    }

    @Override
    public String toString() {
        return String.format("Tour %d | %s | Temps: %ds",
                currentRound, getActiveTeam().getName(), timeLeft);
    }
}
