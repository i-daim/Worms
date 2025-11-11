package org.example.model.items;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Mine extends Arme implements Utilisable {
    public static final int DEGATS = 45;
    public static final int RAYON = 3;
    public static Position POSITION = new Position(0,0); //Variable représentant la position de la mine par défaut
    public Mine(String nom){ super(nom, DEGATS, RAYON); }

    @Override
    public void utiliser(Monde monde, Worm wormActif) {
    }

    //Méthode Poser_mine permettant de poser une mine pour un joueur et met à jour la position de la mine
    public void Poser_mine(Worm wormActif) {
        int wormActif_position_x = wormActif.getPosition().getX();
        int wormActif_position_y = wormActif.getPosition().getY();
        POSITION =  new Position(wormActif_position_x + 1, wormActif_position_y + 1); //+1 pour pas que la mine soit à la même position que son porteur pour éviter qu'il l'active lui-même
    }

    public boolean activation(Monde monde) {
        int position_mine_x = POSITION.getX(); // position de la minde en x
        int position_mine_y = POSITION.getY(); // position de la mine en y
        for ( w Worms : ) { //Remplir par une méthode renvoyant l'ensemble des vers dans le monde/terrain
            int w_position_x =  w.getPosition().getX();
            int w_position_y =  w.getPosition().getY();
            if ( )
        }
    }

}
