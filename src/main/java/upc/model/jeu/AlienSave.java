package upc.model.jeu;

import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.items.Item;

/*Classe permet de sauvegarder les informations minimal d'un alien*/
public class AlienSave {

    //Attribut minimal de sauvegarde de ALien
    private int healthPoints;
    private boolean alive;
    private boolean direction;
    private int aimAngle;
    private int shootPower;

    private Position position;

    //Constructeur
    public AlienSave(Alien alien) {
        this.healthPoints = alien.getHealthPoints();
        this.alive = alien.isAlive();
        this.direction = alien.getDirection();
        this.aimAngle = alien.getAimAngle();
        this.shootPower = alien.getShootPower();
        this.position = alien.getPosition();

    }

    //Getters

    public int getHealthPoints() {
        return healthPoints;
    }

    public boolean getAlive() {
        return alive;
    }

    public boolean getDirection() {
        return direction;
    }

    public int getAimAngle() {
        return aimAngle;
    }

    public int getShootPower() {
        return shootPower;
    }


    public Position getPosition() {
        return position;
    }
}
