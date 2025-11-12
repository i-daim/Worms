package org.example.model.items;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Monde;

public abstract class PiegeExplosif extends Arme implements Utilisable {
    private Position position; //attribut représentant la position du piège
    private boolean active = true; //attribut qui signale si la mine est encore en service ou non ce qui évite les multiples utilisations
    public PiegeExplosif(String nom, int degats, int rayon) {
        super(nom,degats,rayon);
        this.position = new Position(0,0);
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

    //Méthode verificationPosition qui vérifie si le worms se situe sur le piège
    public boolean verificationPosition(Worm w){
        int positionPiegeX = getPosition().getX(); // position du piège en x
        int positionPiegeY = getPosition().getY(); // position du pièege en y
        int wPositionX =  w.getPosition().getX();
        int wPositionY =  w.getPosition().getY();
        return (positionPiegeX == wPositionX && positionPiegeY == wPositionY);
    }

    public boolean dansZone (Worm w){
        int positionPiegeX = getPosition().getX(); // position de la mine en x
        int positionPiegeY = getPosition().getY(); // position de la mine en y
        int wPositionX =  w.getPosition().getX();
        int wPositionY =  w.getPosition().getY();
        boolean position_x = positionPiegeX <= wPositionX && wPositionX <= positionPiegeX + this.getRayonExplosion(); // vérifie que le vers est dans la zone de dégats de la mine en x
        boolean position_y = positionPiegeY <= wPositionY && wPositionY <= positionPiegeY + this.getRayonExplosion(); // vérifie que le vers est dans la zone de dégats de la mine en y
        return position_y && position_x;
    }

    //Méthode Poser_piege permettant de poser un piège explosif pour un joueur et met à jour la position du piège
    public void Poser_piege(Worm wormActif) {
        int wormActifPositionX = wormActif.getPosition().getX();
        int wormActifPositionY = wormActif.getPosition().getY();
        setPosition(new Position(wormActifPositionX + 1, wormActifPositionY + 1)); //+1 pour pas que le piège soit à la même position que son porteur pour éviter qu'il l'active lui-même
    }

    //Méthode activation permettant de voir si un vers se situe à la position d'un piège et provoque son explosion
    public void activation(Monde monde) {
        if (!isActive()) { return; } //si le piège a déjà explosé on ne peut plus la réutilisé
        for (int i = 0; i < 1; i++) { //Remplir par une méthode renvoyant l'ensemble des vers dans le monde/terrain
            Worm w = new Worm(100,new Equipe("Equipe",1),10,10);
            if ( verificationPosition(w) ) { //Vérifie que un vers se situe sur un piège
                explosion(monde); //Provoque l'appel de la méthode explosion ce qui signifie l'explosion du piège
                return;
            }
        }
    }

    //Méthode explosion infligeant des dégats aux vers qui ont une position proche d'un piège
    public void explosion(Monde monde) {
        for ( Worm w : ) { //Remplir par une méthode renvoyant l'ensemble des vers dans le monde/terrain
            if ( this.dansZone(w) ) {
                w.subirDegats(this.getDegats());
                setActive(false); //La mine a explosé donc elle n'est plus en service
            }
        }
    }
}
