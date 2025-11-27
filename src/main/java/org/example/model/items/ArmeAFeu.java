package org.example.model.items;

import org.example.model.entites.Projectile;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public abstract class ArmeAFeu extends Arme implements Utilisable {
    private int nbTir; //attribut représent le nombre de tir/balle disponible par l'arme
    public ArmeAFeu(String nom, int degats, int rayon, int nbTir) {
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
    public void utiliser(Monde monde, Worm wormActif) {
    }

    //Méthode permettant de tirer et infliger des dégâts
    public void tir(Monde monde, Worm wormActif) {
        int wormActifPositionX = wormActif.getPosition().getX();
        int wormActifPositionY = wormActif.getPosition().getY();
        int portee = this.getRayonExplosion();
        if (this.getNbTir() == 0) {
            System.out.println("Le worm n'a plus de munitions !");
            return;
        }
        for (Worm w : monde.wormsDansRayon(wormActifPositionX, wormActifPositionY, portee)) {
            if (w != wormActif) {
                w.receiveDmg(this.getDegats()); // Dégats aux vers dans la portée de l'arme
                setNbTir(this.getNbTir() - 1); // Tir on décrémente le nombre de tir pour l'arme
            }
        }
    }


}
