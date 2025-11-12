package org.example.model.items;

import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Monde;

public abstract class ArmeAFeu extends Arme implements Utilisable{
    private int nb_tir; //attribut représent le nombre de tir/balle disponible par l'arme
    public ArmeAFeu(String nom, int degats, int rayon, int nb_tir) {
        super(nom, degats, rayon);
        this.nb_tir = nb_tir;
    }

    //getter
    public int getNb_tir() {
        return nb_tir;
    }

    //setter
    public void setNb_tir(int nb_tir) {
        this.nb_tir = nb_tir;
    }

    @Override
    public void utiliser(Monde monde, Worm wormActif) {
    }

    //Méthode permettant de tirer et infliger des dégâts
    public void tir(Monde monde, Worm wormActif) {
        int wormActifPositionX = wormActif.getPosition().getX();
        int wormActifPositionY = wormActif.getPosition().getY();
        int portee = this.getRayonExplosion();
        if (this.getNb_tir() == 0){
            System.out.println("Le worm n'a plus de munitions !"); //précise au joeur l'absence de munitions
            return;
        }
        for (Worm w : monde.wormsDansRayon(wormActifPositionX, wormActifPositionY, portee)){
            if ( w != wormActif){
                w.subirDegats(this.getDegats());// Dégats subis aux vers qui se trouvent dans la portée de l'arme
                setNb_tir(this.getNb_tir() - 1);// Tir donc on décrémente le nombre de tir disponible pour l'arme
            }
        }
    }
}
