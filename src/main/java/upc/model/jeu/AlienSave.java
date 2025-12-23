package upc.model.jeu;

import upc.model.entites.Alien;
import upc.model.items.Item;

/*Classe permet de sauvegarder les informations minimal d'un alien*/
public class AlienSave {

    //Attribut minimal de sauvegarde de ALien
    private int healthPoints;
    private boolean alive;
    private boolean direction;
    private int aimAngle;
    private int shootPower;

    private Item.Type equippedItem;

    //Constructeur
    public AlienSave(Alien alien) {
        this.healthPoints = alien.getHealthPoints();
        this.alive = alien.isAlive();
        this.direction = alien.getDirection();
        this.aimAngle = alien.getAimAngle();
        this.shootPower = alien.getShootPower();
        this.equippedItem = alien.getEquippedItem();

    }
}
