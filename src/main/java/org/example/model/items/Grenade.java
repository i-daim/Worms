package org.example.model.items;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Grenade extends Arme implements Utilisable {
    public static final int DEGATS = 45;
    public static final int RAYON = 3;

    public Grenade(String nom) {
        super(nom,DEGATS,RAYON);
    }

    @Override
    public void utiliser(Monde monde, Worm wormActif) {
    }

    //Méthode lancer qui renvoie la position de la grenade après que un joueur la lance 
    public Position lancer(Worm wormActif) { }

    //Méthode explosion infligeant des dégats aux vers qui ont une position proche de la grenade
    public void explosion(Monde monde, Worm wormActif) {
        Position position_grenade = lancer(wormActif); // représente la position de la grenade après qu'un joueur l'est lanceé
        int position_grenade_x = position_grenade.getX(); // position de la grenade en x
        int position_grenade_y = position_grenade.getY(); // position de la grenade en y
        for ( w Worm : ) {
            int w_position_x =  w.getPosition().getX();
            int w_position_y =  w.getPosition().getY();
            boolean position_x = position_grenade_x <= w_position_x && w_position_x <= position_grenade_x + RAYON; // vérifie que le vers est dans la zone de dégats de la grenade en x
            boolean position_y = position_grenade_y <= w_position_y && w_position_y <= position_grenade_y + RAYON; // vérifie que le vers est dans la zone de dégats de la grenade en y
            if ( position_x && position_y ) {
                w.subirDegats(DEGATS);
            }
        }
    }
}
