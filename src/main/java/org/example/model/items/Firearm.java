package org.example.model.items;

import org.example.model.entites.Alien;
import org.example.model.jeu.GameWorld;

public abstract class Firearm extends Weapon implements Usable {
    private int nbTir; //attribut représent le nombre de tir/balle disponible par l'arme
    public Firearm(String nom, int degats, int rayon, int nbTir) {
        super(nom, degats, rayon);
        this.nbTir = nbTir;
    }

    //getter
    public int getNbTir() {
        return nbTir;
    }

    //setter
    public void setNbTir(int nbTir) {
        this.nbTir = nbTir;
    }

    @Override
    public void utiliser(GameWorld gameWorld, Alien alienActif) {
    }

    //Méthode permettant de tirer et infliger des dégâts
    public void tir(GameWorld gameWorld, Alien alienActif) {
        int wormActifPositionX = alienActif.getPosition().getX();
        int wormActifPositionY = alienActif.getPosition().getY();
        int portee = this.getRayonExplosion();
        if (this.getNbTir() == 0) {
            System.out.println("Le worm n'a plus de munitions !");
            return;
        }
        for (Alien w : gameWorld.aliensInRadius(wormActifPositionX, wormActifPositionY, portee)) {
            if (w != alienActif) {
                setNbTir(this.getNbTir() - 1); // Tir on décrémente le nombre de tir pour l'arme
            }
        }
    }
    @Override
    public double getInitialVelocity() {
        return 35.0;
    }
    @Override
    public boolean isAffectedByWind() {
        return false;
    }

}
