package org.example.model.items;

import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Shotgun extends ArmeAFeu {
    public static final int DEGATS = 25;
    public static final int RAYON = 5;
    public static final int NBTIR = 2;

    public Shotgun(String nom) {
        super(nom, DEGATS, RAYON, NBTIR);
    }

    @Override
    //Méthode permettant de tirer et infliger des dégâts
    public void tir(Monde monde, Worm wormActif) {
        int wormActifPositionX = wormActif.getPosition().getX();
        int wormActifPositionY = wormActif.getPosition().getY();
        for (Worm w : monde.wormsDansRayon(wormActifPositionX, wormActifPositionY, RAYON)) {
            if (w != wormActif) {
                w.subirDegats(DEGATS);
            }
        }
    }
}
