package upc.model.jeu;

import upc.assets.SoundManager;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.assets.ActiveAnimation;
import upc.assets.Animation;
import upc.assets.AnimationManager;
import upc.model.entites.projectiles.Projectile;
import upc.model.entites.projectiles.Trap;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameManager {
    /** Constantes */
    public static final int ROUND_TIME = 60;
    public static final int TEAM_SIZE = 2;

    private Terrain terrain;
    private final String mapName;
    private final GameWorld gameWorld;
    private Team[] teams;

    /** Attribut en lien à la partie */
    private int activeTeamIndex;
    private int currentRound;
    private int timeLeft;
    private boolean gameOver;
    private boolean actionDone;

    private final List<ActiveAnimation> activeAnimations;
    private Projectile activeProjectile;
    private long delayEndTime = 0;
    private boolean isPaused = false;
    private Runnable onDelayFinished;
    private boolean windEnabled = false;


    /**
     * Constructeur pour {@code GameManager}
     * @param terrain : Terrain de jeu
     * @param teamSize : taille des équipes
     */
    public GameManager(Terrain terrain, int teamSize) {
        this.terrain = terrain;
        this.mapName = terrain.getMapName();

        this.teams = new Team[2];
        this.teams[0] = new Team("Zlerps", TEAM_SIZE);
        this.teams[1] = new Team("Glorbs", TEAM_SIZE);

        this.gameWorld = new GameWorld(this, this.terrain);
        this.activeAnimations = new CopyOnWriteArrayList<>();

        initializeGame();
    }

    /**
     * Constructeur pour {@code GameManager}
     * @param mapName : Crée le terrain de jeu selon le nom de terrain donnée
     */
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



    /// Gestion de la partie ////


    /**
     * Méthode permettant de mettre à jour le gestionnaire du jeu
     * @param elapsedTime : temps écoulé depuis la dernière mise à jour
     */
    public void update(double elapsedTime) {
        // On met à jour les physiques et animations
        gameWorld.update(elapsedTime);
        updateAnimations();
        checkGameOver();
        checkActiveProjectile();

        // Si le délai n'est pas fini, on met pas à jour le reste
        if (System.currentTimeMillis() < delayEndTime) {
            return;
        }
        // Exécute les commandes à exécuter après le délai
        if (isPaused) {
            isPaused = false;
            if (onDelayFinished != null) {
                onDelayFinished.run();
                onDelayFinished = null;
            }
        }
        //Vérifie que le vers controlé est toujours en vie sinon on passe à l'adversaire
        if (getActiveAlien() == null || !getActiveAlien().isAlive()) {
            skipTurn();
            return;
        }
        countTimeLeft();
    }

    /**
     * Méthode permettant d'initialiser la partie
     */
    private void initializeGame() {

        //Initialise les équipes dans le terrain de jeu
        teams[0].initializeTeam(gameWorld, mapName, 0);
        teams[1].initializeTeam(gameWorld, mapName, 1);

        //Initialise la physique du jeu
        gameWorld.initializePhysics();

        //Lance les attributs en lien avec la partie
        this.activeTeamIndex = 0;
        this.currentRound = 1;
        this.timeLeft = ROUND_TIME;
        this.gameOver = false;
        this.actionDone = false;

        setDelay(3000, () -> SoundManager.playVoice(0));
    }

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

        setDelay(2000, () -> {
            nextTurn();
            timeLeft = ROUND_TIME;
        });
    }

    /**
     * Méthode permettant de passser au prochain tour
     */
    private void nextTurn() {
        // Passe à l'alien suivant avant de changer d'équipe, pour le prochain tour
        getActiveTeam().skipToNextAlien();

        // Passe à l'équipe suivante
        activeTeamIndex = (activeTeamIndex + 1) % teams.length;

        // [MODIFICATION] Délégation à la physique
        if (windEnabled) {
            applyWindChange();
        }

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

    /**
     * Méthode permettant de vérifier si une partie est finie ou non
     * Parcourt les équipes et si une est éliminée
     * Change l'attrbut gameOver en true
     */
    private void checkGameOver() {
        for (Team team : teams) {
            if (team.isEliminated()) {
                gameOver = true;
            }
        }
    }

    /// Autres méthodes ///

    //Attribut compteur de frame
    private int frameCounter = 0;

    /**
     * Méthode permettant de décrementer le temps restant de la partie
     */
    public void countTimeLeft() {
        frameCounter++;
        if (frameCounter >= 60) {  // 1 seconde à 60 FPS
            frameCounter = 0;

            //Si le temps restant est supérieur à 0
            if (timeLeft > 0) {

                //Décremente de 1
                timeLeft--;
            } else {
                //Alors on passe au prochain tour
                skipTurn();
            }
        }
    }

    /**
     * Méthode permettant d'infliger des dégats à un alien à une position donnée
     * @param x : Donne une position en x
     * @param y : Donne une position en y
     * @param damage : Dégat à infliger
     * @return l'objet {@link Alien} ciblé
     * ou {@code null} si les dégats sélectionnés sont invalide ou actionDone est vrai
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
     * @param name : nom de l'aniamtion
     * @param posX : coordonnée en x
     * @param posY : coordonnée en y
     */
    public void addActiveAnimation(String name, int posX, int posY) {
        Animation animation = AnimationManager.getAnimation(name);
        ActiveAnimation active = new ActiveAnimation(animation, 50, false, posX, posY);
        activeAnimations.add(active);
    }

    /**
     * Méthode permettant de définir le délai de pause du jeu
     * @param durationMs : durée du délai en millisecondes
     * @param onFinished : Action à exécuter à la fin du délai
     * */
    public void setDelay(long durationMs, Runnable onFinished) {
        this.delayEndTime = System.currentTimeMillis() + durationMs;
        this.isPaused = true;
        this.onDelayFinished = onFinished;
    }

    /**
     * Méthode permettant de vérifier l'état du projectile actif
     * selon qu'il soit de type piège ou en vol
     * */
    private void checkActiveProjectile() {
        if (activeProjectile == null || activeProjectile instanceof Trap) {
            activeProjectile = null;
            return;
        }
        if (activeProjectile.getState().equals(Projectile.State.FLYING)) {
            delayEndTime += 20;
        } else {
            activeProjectile = null;
        }
    }


    /**
     * Méthode simplifiée qui demande au moteur physique de faire le travail
     */
    private void applyWindChange() {
        if (gameWorld.getPhysicsEngine() != null) {
            gameWorld.getPhysicsEngine().randomizeWind();
        }
    }


    ////    GETTERS & SETTERS    ////

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
     * @return {@code null}
     * ou l'objet {@link Team} si l'index est valide
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
     * Retourne un boolean si une partie est en pause
     * @return l'attribut isPaused
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Retourne le projectile active
     * @return l'attribut activeProjectile
     */
    public Projectile getActiveProjectile() {
        return activeProjectile;
    }

    /**
     * Retourne un boolean si le vent est activé dans le jeu
     * @return l'attribut windEnabled
     */
    public boolean isWindEnabled() {
        return windEnabled;
    }


    /**
     * Retourne l'équipe gagnante
     * @return {@code null} si gameOver est false
     * ou l'objet {@link Team} représentant l'équipe non éliminée
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
     * @return un entier {@link int} représentant le nombre d'aliens vivants dans une équipe
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
     * @return {@code null}
     * ou l'objet {@link Alien} si l'alien a une position égale à celle donnée en paramètre
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


    /**
     * Permet de changer le projective active
     * selon qu'il soit de type piège
     * */
    public void setActiveProjectile(Projectile p) {
        if (activeProjectile instanceof Trap) {
            return;
        }
        this.activeProjectile = p;
    }


    /**
     * Permet d'activer ou de désactiver le vent
     */
    public void setWindEnabled(boolean enabled) {
        this.windEnabled = enabled;
        if (enabled) {
            applyWindChange();
        }
    }



    /**
     * Modifie le terrain du jeu
     * @param terrain : Pour sélectionner un terrrain
     */
    public void setMap(Terrain terrain) {
        this.terrain = terrain;
        this.gameWorld.getPhysicsEngine().setTerrain(terrain);
    }

    /**
     * Modifie les équipes du jeu
     * @param teams : Pour sélectionner les équipes
     */
    public void setTeams(Team[] teams) {
        this.teams = teams;
    }


    /**
     * Retourne le temps restant avant la fin du délai en cours
     */
    public int getDelay() {
        return (int) (delayEndTime - System.currentTimeMillis()) / 10;
    }

}




