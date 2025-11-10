package org.example.model.items;

import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Shotgun extends Arme implements Utilisable {
    public static final int DEGATS = 25;
    public static final int RAYON = 5;

    public Shotgun(String nom) {
        super(nom,DEGATS,RAYON);
    }

    @Override
    public void utiliser(Monde monde, Worm wormActif) {
    }

    //Méthode permettant de tirer et infliger des dégâts
    public void tir(Monde monde, Worm wormActif) {
        int wormActif_position_x = wormActif.getPosition().getX();
        int wormActif_position_y = wormActif.getPosition().getY();
        for ( Worm w : ){ //Remplir par une méthode renvoyant l'ensemble des vers dans le monde/terrain
            int w_position_x =  w.getPosition().getX();
            int w_position_y =  w.getPosition().getY();
            if ( w_position_x == wormActif_position_x + 1 || w_position_y == wormActif_position_y + 1 && w != wormActif ){
                w.subirDegats(DEGATS);     // Dégats subis différents selon la distance car dans le jeu worms selon la distance que se trouve l'ennemi de pour cette arme les dégats subis peuvent varier
            }
            if ( w_position_x  == wormActif_position_x + 2 || w_position_y == wormActif_position_y + 2 && w != wormActif ){
                w.subirDegats(DEGATS - 5 );
            }
            if ( w_position_x == wormActif_position_x + 3 || w_position_y == wormActif_position_y + 3 && w != wormActif){
                w.subirDegats(DEGATS - 10);
            }
            if ( w_position_x == wormActif_position_x + 4 || w_position_y == wormActif_position_y + 4 && w != wormActif){
                w.subirDegats(DEGATS - 15);
            }
            if (w_position_x == wormActif_position_x + 5 || w_position_y == wormActif_position_y + 5 && w != wormActif){
                w.subirDegats(DEGATS - 20);
            }
        }
    }

}
