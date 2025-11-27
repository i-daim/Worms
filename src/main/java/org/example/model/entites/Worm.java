package org.example.model.entites;

import org.example.model.jeu.Equipe;
import org.example.model.jeu.Terrain;
import org.example.view.gui.GamePanel;

public class Worm extends Entite {
    // Attributs
    private final Equipe team;
    private int healthPoints;
    private boolean alive;

    // Champs statiques
    private static final int SPEED = 5;
    private static final int JUMP = -20;
    private static final int INITIAL_HP = 100;


    // Constructeur
    public Worm(Equipe team, Position position) {
        super(position.getX(), position.getY(), SPEED);
        this.healthPoints = INITIAL_HP;
        this.team = team;
        this.alive = true;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public boolean isAlive() {
        return alive;
    }

    public Equipe getTeam() {
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

    public boolean move(Position newPosition, Terrain terrain) {
        if (!alive) {
            return false;
        }

        if (terrain.possibleMove(newPosition)) {
            setPosition(newPosition);
            return true;
        }

        return false;
    }

    public boolean moveRight(Terrain terrain) {
        Position newPosition = getPosition().deplacer(SPEED, 0);
        return move(newPosition, terrain);
    }

    public boolean moveLeft(Terrain terrain) {
        Position newPosition = getPosition().deplacer(-SPEED, 0);
        return move(newPosition, terrain);
    }


    public boolean jump(Terrain terrain) {
        Position newPosition = getPosition().deplacer(0, JUMP);
        return move(newPosition, terrain);
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

    @Override
    public String toString() {
        return "Worm[PV=" + healthPoints + ", Vivant=" + alive + "]";
    }

}
