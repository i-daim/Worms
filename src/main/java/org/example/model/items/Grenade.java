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

    public Position lancer(Worm wormActif) { }

    public void explosion(Monde monde, Worm wormActif) {
        Position position_grenade = lancer(wormActif);
        int position_grenade_x = position_grenade.getX();
        int position_grenade_y = position_grenade.getY();
        for ( w Worm : ) {
            int w_position_x =  w.getPosition().getX();
            int w_position_y =  w.getPosition().getY();
            boolean position_x = position_grenade_x <= w_position_x && w_position_x <= position_grenade_x + RAYON;
            boolean position_y = position_grenade_y <= w_position_y && w_position_y <= position_grenade_y + RAYON;
            if ( position_x && position_y ) {
                w.subirDegats(DEGATS);
            }
        }
    }
}
