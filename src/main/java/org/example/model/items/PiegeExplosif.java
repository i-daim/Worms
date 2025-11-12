package org.example.model.items;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public abstract class PiegeExplosif extends Arme implements Utilisable {
    private Position position; //attribut représentant la position du piège
    private boolean active = true; //attribut qui signale si la mine est encore en service ou non ce qui évite les multiples utilisations

    public PiegeExplosif(String nom, int degats, int rayon) {
        super(nom, degats, rayon);
        this.position = new Position(0, 0);
    }

    @Override
    public void utiliser(Monde monde, Worm wormActif) {
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

    //Méthode Poser_piege permettant de poser un piège explosif pour un joueur et met à jour la position du piège
    public void Poser_piege(Worm wormActif) {
        int wormActifPositionX = wormActif.getPosition().getX();
        int wormActifPositionY = wormActif.getPosition().getY();
        setPosition(new Position(wormActifPositionX + 1, wormActifPositionY + 1)); //+1 pour pas que le piège soit à la même position que son porteur pour éviter qu'il l'active lui-même
    }

    //Méthode activation permettant de voir si un vers se situe à la position d'un piège et provoque son explosion
    public void activation(Monde monde) {
        if (!isActive()) {
            return;
        } //si le piège a déjà explosé on ne peut plus la réutilisé
    }

    //Méthode explosion infligeant des dégats aux vers qui ont une position proche d'un piège
    public void explosion(Monde monde) {
        for (Worm w : monde.wormsDansRayon( this.getPosition().getX(), this.getPosition().getY(), this.getRayonExplosion()) ) { //Remplir par une méthode renvoyant l'ensemble des vers dans le monde/terrain

        }
    }
}
