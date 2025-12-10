package upc.model.entites;

import upc.model.jeu.GameWorld;
import upc.model.jeu.Team;
import upc.model.physics.AlienPhysics;
import upc.view.gui.GamePanel;

public class Alien extends Entity {
    // Attributs
    private final AlienPhysics alienPhysics;
    private final Team team;
    private int healthPoints;
    private boolean alive;
    private boolean direction;

    // Champs statiques
    private static final int SPEED = 5;
    private static final int INITIAL_HP = 100;

    // Etat de l'alien (pour la partie graphique)
    public enum State {
        IDLE,
        WALK,
        JUMP
    }

    private State state;



    // Constructeur
    public Alien(GameWorld gameWorld, Team team, Position position) {
        super(position, SPEED);
        this.healthPoints = INITIAL_HP;
        this.alienPhysics = gameWorld.getPhysicsEngine().getAlienPhysics();
        this.team = team;
        this.alive = true;
        this.state = State.IDLE;
        this.direction = true;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public boolean isAlive() {
        return alive;
    }

    public Team getTeam() {
        return team;
    }

    public void receiveDmg(int damage) {
        if (!alive) {
            return;
        }
        if (damage < 0) {
            damage = 0;
        }
        healthPoints -= damage;

        if (healthPoints <= 0) {
            healthPoints = 0;
            alive = false;
        }
    }

    public void moveRight() {
        alienPhysics.moveAlien(this, 1);
        setState(State.WALK);
        direction = true;
    }

    public void moveLeft() {
        alienPhysics.moveAlien(this, -1);
        setState(State.WALK);
        direction = false;
    }


    public void jump() {
        alienPhysics.jumpAlien(this);
        setState(State.JUMP);
    }

    public void idle() {
        alienPhysics.moveAlien(this, 0);
        setState(State.IDLE);
    }

    public boolean isVisible(int cameraX, int cameraY) {
        if (!alive) {
            return false;
        }

        int posX = getPosition().getX();
        int posY = getPosition().getY();

        int margin = 200;

        return posX >= cameraX - margin
                && posX <= cameraX + GamePanel.DEFAULT_WIDTH + margin
                && posY >= cameraY - margin
                && posY <= cameraY + GamePanel.DEFAULT_HEIGHT + margin;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean getDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }
}
