package org.example.model.items;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Mine extends Arme implements Utilisable {
    public static final int DEGATS = 45;
    public static final int RAYON = 3;

    public Mine(String nom){ super(nom, DEGATS, RAYON); }

    @Override
    public void utiliser(Monde monde, Worm wormActif) {
    }

    //Méthode Poser_mine permettant de poser une mine pour un joueur et renvoyant la nouvelle position de la mine
    public Position Poser_mine(Worm wormActif) {
        int wormActif_position_x = wormActif.getPosition().getX();
        int wormActif_position_y = wormActif.getPosition().getY();
        return new Position(wormActif_position_x + 1, wormActif_position_y + 1); //+1 pour pas que la mine soit à la même position que son porteur pour éviter qu'il l'active lui-même
    }



}
