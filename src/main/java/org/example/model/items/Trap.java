package org.example.model.items;

import org.example.model.entites.Alien;
import org.example.model.entites.Position;
import org.example.model.jeu.GameWorld;

public abstract class Trap extends Weapon implements Usable {
    private Position position; //attribut représentant la position du piège
    private boolean active = true; //attribut qui signale si la mine est encore en service ou non

    public Trap(String nom, int degats, int rayon) {
        super(nom, degats, rayon);
        this.position = new Position(0, 0);
    }

    @Override
    public void utiliser(GameWorld gameWorld, Alien alienActif) {
    }
    @Override
    public double getInitialVelocity() {
        return 0;
    }
    @Override
    public boolean isAffectedByWind() {
        return false;
    }
    //getters
    public Position getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    //setters
    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    //Méthode poserPiege permettant de poser un piège explosif et met à jour la position du piège
    public void poserPiege(Alien alienActif) {
        int wormActifPositionX = alienActif.getPosition().getX();
        int wormActifPositionY = alienActif.getPosition().getY();
        //+1 le piège ne doit pas être à la même position du porteur
        setPosition(new Position(wormActifPositionX + 1, wormActifPositionY + 1));
    }

    //Méthode activation voir si un vers se situe à la position d'un piège et provoque son explosion
    public void activation(GameWorld gameWorld) {
        int posPiegeX = this.getPosition().getX();
        int posPiegeY = this.getPosition().getY();
        if (!isActive()) { //si le piège a déjà explosé on ne peut plus la réutilisé
            return;
        }
        for (Alien w : gameWorld.wormsDansRayon(posPiegeX, posPiegeY, this.getRayonExplosion())) {
            if (w.getPosition().getX() == posPiegeX && w.getPosition().getY() == posPiegeY) {
                explosion(gameWorld);
            }
        }
    }

    //Méthode explosion infligeant des dégats aux vers qui ont une position proche d'un piège
    public void explosion(GameWorld gameWorld) {
        int posPiegeX = this.getPosition().getX();
        int posPiegeY = this.getPosition().getY();
        for (Alien w : gameWorld.wormsDansRayon(posPiegeX, posPiegeY, this.getRayonExplosion())) {
            if (w == null) {
                return;
            }
            this.active = false;
        }
    }

}
