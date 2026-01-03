package upc.model.jeu;

import upc.assets.SoundManager;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.assets.ActiveAnimation;
import upc.assets.Animation;
import upc.assets.AnimationManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameManager {
    public static final int ROUND_TIME = 60;
    public static final int TEAM_SIZE = 2;

    private Terrain terrain;

    private final String mapName;

    private final GameWorld gameWorld;
    private Team[] teams;

    private int activeTeamIndex;
    private int currentRound;
    private int timeLeft;
    private boolean gameOver;
    private boolean actionDone;

    private final List<ActiveAnimation> activeAnimations;

    public GameManager() {
        this.terrain = TerrainGenerator.chooseRandomMap();
        this.mapName = terrain.getMapName();

        this.teams = new Team[2];
        this.teams[0] = new Team("Zlerps", TEAM_SIZE);
        this.teams[1] = new Team("Glorbs", TEAM_SIZE);

        this.gameWorld = new GameWorld(this, this.terrain);
        this.activeAnimations = new CopyOnWriteArrayList<>();

        initializeGame();
    }
    public GameManager(String mapName) {
        this.terrain = TerrainGenerator.createTerrain(mapName);
        this.mapName = mapName;

        this.teams = new Team[2];
        this.teams[0] = new Team("Zlerps", TEAM_SIZE);
        this.teams[1] = new Team("Glorbs", TEAM_SIZE);

        this.gameWorld = new GameWorld(this, this.terrain);
        this.activeAnimations = new CopyOnWriteArrayList<>();

        initializeGame();
    }


    public void update(double elapsedTime) {
        //Vérifie que le vers controlé est toujours en vie sinon on passe à l'adversaire
        if (getActiveAlien() == null || !getActiveAlien().isAlive()) {
            skipTurn();
            return;
        }
        gameWorld.update(elapsedTime);

        updateAnimations();

        checkGameOver();

        countTimeLeft();
    }

    private void initializeGame() {
        teams[0].initializeTeam(gameWorld, mapName, 0);
        teams[1].initializeTeam(gameWorld, mapName, 1);

        gameWorld.initializePhysics();

        this.activeTeamIndex = 0;
        this.currentRound = 1;
        this.timeLeft = ROUND_TIME;
        this.gameOver = false;
        this.actionDone = false;

        SoundManager.playVoice(0);
    }

    /// Gestion des tours ////

    /**
     * Méthode permettant de passser son tour volontairement
     */
    public void skipTurn() {

        // ne rien faire si la partie est terminée
        if (gameOver) {
            return;
        }

        //Annule l'action de l'alien acitve
        actionDone = false;

        //Incrémente le nombre de tour si partie non finie
        if (getTeam(0).isActiveLastAlien() && getTeam(1).isActiveLastAlien()) {
            currentRound++;
        }

        //Permet de passer au prochain tour
        nextTurn();

        //Reinitialise le temps de jeu
        timeLeft = ROUND_TIME;
    }

    /**
     * Méthode permettant de passser au prochain tour
     */
    private void nextTurn() {
        // Passe à l'alien suivant avant de changer d'équipe, pour le prochain tour
        getActiveTeam().skipToNextAlien();

        // Passe à l'équipe suivante
        activeTeamIndex = (activeTeamIndex + 1) % teams.length;

        if (!getActiveTeam().isEliminated()) {
            // Pour passer au prochain alien vivant si l'actif est mort
            while (getActiveAlien() != null && !getActiveAlien().isAlive()) {
                getActiveTeam().skipToNextAlien();
            }
            SoundManager.playVoice();
        } else {
            gameOver = true;
        }
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


    /**
     * Méthode permettant de vérifier si une partie est finie ou non
     * Parcourt les équipes et si une est éliminée
     * Change l'attrbut gameOver en true
     */
    private void checkGameOver() {
        for (Team team : teams) {
            if (team.isEliminated()) {
                gameOver = true;
                return;
            }
        }
    }

    /**
     * Méthode permettant d'infliger des dégats à un alien à une position donnée
     * @param x : Donne une position en x
     * @param y : Donne une position en y
     * @param damage : Dégat à infliger
     * */
    public Alien attackPosition(int x, int y, int damage) {
        if (actionDone || damage < 0) {
            return null;
        }

        Alien target = getAlienAtPosition(x, y);

        //Si un alien existe à la position donnée
        if (target != null) {

            //Inflige des dégats
            target.receiveDmg(damage);

            //Vérifie si partie est finie suite à ça
            checkGameOver();

            actionDone = true;
        }

        //Renvoie l'alien ciblé
        return target;
    }

    /// Animation ///

    /**
     * Méthode permettant de mettre à jour l'animation
     */
    private void updateAnimations() {
        activeAnimations.removeIf(active -> {
            active.update();
            return active.isDone();
        });
    }

    /**
     * Méthode permettant d'ajouter une animation active dans la liste d'animations
     */
    public void addActiveAnimation(String name, int posX, int posY) {
        Animation animation = AnimationManager.getAnimation(name);
        ActiveAnimation active = new ActiveAnimation(animation, 50, false, posX, posY);
        activeAnimations.add(active);
    }


////    GETTERS    ////

    /**
     * Retourne le terrain du jeu
     * @return L'attribut terrain
     */
    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * Retourne le
     * @return L'attribut gameWorld
     */
    public GameWorld getWorld() {
        return gameWorld;
    }

    /**
     * Retourne l'équipe active
     * @return l'équipe depuis l'attribut teams
     */
    public Team getActiveTeam() {
        return teams[activeTeamIndex];
    }

    /**
     * Retourne l'équipe ennemie
     * @return l'équipe depuis l'attribut teams
     */
    public Team getEnnemyTeam() {
        int indexEnnemi = (activeTeamIndex + 1) % teams.length;
        return teams[indexEnnemi];
    }

    /**
     * Retourne l'équipe à l'indice donnée
     * @param index : Pour sélectionner l'équipe équipe à la position teams[index]
     */
    public Team getTeam(int index) {
        if (index >= 0 && index < teams.length) {
            return teams[index];
        }
        return null;
    }

    /**
     * Retourne les équipes du jeu
     * @return l'attribut teams
     */
    public Team[] getTeams() {
        return teams;
    }

    /**
     * Retourne l'indice de l'équipe active
     * @return l'attribut activeTeamIndex
     */
    public int getActiveTeamIndex() {
        return activeTeamIndex;
    }

    /**
     * Retourne l'alien active
     * @return l'alien acitve depuis l'équipe ative
     */
    public Alien getActiveAlien() {
        return getActiveTeam().getActiveAlien();
    }

    /**
     * Retourne le n-ième round joué
     * @return l'attribut currentRound
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Retourne le temps restant
     * @return l'attribut timeLeft
     */
    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * Retourne un boolean si une partie est finie
     * @return l'attribut gameOver
     */
    public boolean isOver() {
        return gameOver;
    }

    /**
     * Retourne l'équipe gagnante
     * @return l'équipe qui n'est pas éliminée
     */
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

    /**
     * Retourne le nombre de aliens vivants
     * @return un entier représentant le nombre d'aliens vivants dans une équipe
     */
    public int getAliveWorms() {
        int total = 0;
        for (Team team : teams) {
            total += team.getAliveAliensNumber();
        }
        return total;
    }


    /**
     * Retourne un boolean si l'alien effectue une action
     * @return l'attribut actionDone
     */
    public boolean isActionDone() {
        return actionDone;
    }

    /**
     * Retourne une liste des animations de l'alien
     * @return l'attribut activeAnimations
     */
    public List<ActiveAnimation> getActiveAnimations() {
        return activeAnimations;
    }

    /**
     * Retourne un alien si il se trouve à une position donnée
     * @param x : Donne une position en x
     * @param y : Donne une position en y
     * @return l'alien qui a une position égale à celle donnée en paramètre
     */
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

    /// SETTERS ///

    /**
     * Modifie le terrain du jeu
     * @param terrain : Pour sélectionner un terrrain
     */
    public void setMap(Terrain terrain) {
        this.terrain = terrain;
    }

    /**
     * Modifie les équipes du jeu
     * @param teams : Pour sélectionner les équipes
     */
    public void setTeams(Team[] teams) {
        this.teams = teams;
    }

}