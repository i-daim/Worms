package org.example.model.items;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public abstract class Pieges_explosifs extends Arme implements Utilisable {
    private Position position; //attribut représentant la position du piège
    private boolean active = true; //attribut qui signale si la mine est encore en service ou non ce qui évite les multiples utilisations
    public Pieges_explosifs(String nom, int degats, int rayon) {
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

    //Méthode Poser_piege permettant de poser un piège explosif pour un joueur et met à jour la position du piège
    public void Poser_piege(Worm wormActif) {
        int wormActif_position_x = wormActif.getPosition().getX();
        int wormActif_position_y = wormActif.getPosition().getY();
        setPosition(new Position(wormActif_position_x + 1, wormActif_position_y + 1)); //+1 pour pas que le piège soit à la même position que son porteur pour éviter qu'il l'active lui-même
    }

    //Méthode activation permettant de voir si un vers se situe à la position d'un piège et provoque son explosion
    public void activation(Monde monde) {
        int position_piege_x = getPosition().getX(); // position de la mine en x
        int position_piege_y = getPosition().getY(); // position de la mine en y
        if (!isActive()) { return; } //si le piège a déjà explosé on ne peut plus la réutilisé
        for ( Worm w : ) { //Remplir par une méthode renvoyant l'ensemble des vers dans le monde/terrain
            int w_position_x =  w.getPosition().getX();
            int w_position_y =  w.getPosition().getY();
            if ( position_piege_x == w_position_x && position_piege_y == w_position_y ) { //Vérifie que un vers se situe sur un piège
                explosion(monde); //Provoque l'appel de la méthode explosion ce qui signifie l'explosion du piège
                return;
            }
        }
    }

    //Méthode explosion infligeant des dégats aux vers qui ont une position proche d'un piège
    public void explosion(Monde monde) {
        int position_piege_x = getPosition().getX(); // position de la mine en x
        int position_piege_y = getPosition().getY(); // position de la mine en y
        for ( Worm w : ) { //Remplir par une méthode renvoyant l'ensemble des vers dans le monde/terrain
            int w_position_x =  w.getPosition().getX();
            int w_position_y =  w.getPosition().getY();
            boolean position_x = position_piege_x <= w_position_x && w_position_x <= position_piege_x + RAYON; // vérifie que le vers est dans la zone de dégats de la mine en x
            boolean position_y = position_piege_y <= w_position_y && w_position_y <= position_piege_y + RAYON; // vérifie que le vers est dans la zone de dégats de la mine en y
            if ( position_x && position_y ) {
                w.subirDegats(this.getDegats());
                setActive(false); //La mine a explosé donc elle n'est plus en service
            }
        }
    }
}
