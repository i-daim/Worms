package org.example.model.items;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Mine extends Arme implements Utilisable {
    public static final int DEGATS = 45;
    public static final int RAYON = 3;
    public Position position; //attribut représentant la position de la mine par défaut
    public Mine(String nom){ super(nom, DEGATS, RAYON); this.position = new Position(0,0); }

    @Override
    public void utiliser(Monde monde, Worm wormActif) {
    }

    //Méthode Poser_mine permettant de poser une mine pour un joueur et met à jour la position de la mine
    public void Poser_mine(Worm wormActif) {
        int wormActif_position_x = wormActif.getPosition().getX();
        int wormActif_position_y = wormActif.getPosition().getY();
        this.position =  new Position(wormActif_position_x + 1, wormActif_position_y + 1); //+1 pour pas que la mine soit à la même position que son porteur pour éviter qu'il l'active lui-même
    }

    //Méthode activation permettant de voir si un vers se situe à la position d'une mine et provoque son explosion
    public void activation(Monde monde) {
        int position_mine_x = position.getX(); // position de la mine en x
        int position_mine_y = position.getY(); // position de la mine en y
        for ( w Worms : ) { //Remplir par une méthode renvoyant l'ensemble des vers dans le monde/terrain
            int w_position_x =  w.getPosition().getX();
            int w_position_y =  w.getPosition().getY();
            if ( position_mine_x == w_position_x && position_mine_y == w_position_y ) { //Vérifie que un vers se situe sur la mine
                explosion(monde); //Provoque l'appel de la méthode explosion ce qui signifie l'explosion de la mine
                return;
            }
        }
    }

    //Méthode explosion infligeant des dégats aux vers qui ont une position proche de la mine
    public void explosion(Monde monde) {
        int position_mine_x = position.getX(); // position de la mine en x
        int position_mine_y = position.getY(); // position de la mine en y
        for ( Worm w : ) { //Remplir par une méthode renvoyant l'ensemble des vers dans le monde/terrain
            int w_position_x =  w.getPosition().getX();
            int w_position_y =  w.getPosition().getY();
            boolean position_x = position_mine_x <= w_position_x && w_position_x <= position_mine_x + RAYON; // vérifie que le vers est dans la zone de dégats de la mine en x
            boolean position_y = position_mine_y <= w_position_y && w_position_y <= position_mine_y + RAYON; // vérifie que le vers est dans la zone de dégats de la mine en y
            if ( position_x && position_y ) {
                w.subirDegats(DEGATS);
            }
        }
    }

}
