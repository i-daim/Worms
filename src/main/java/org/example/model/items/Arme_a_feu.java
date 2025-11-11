package org.example.model.items;

import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public class Arme_a_feu extends Arme implements Utilisable{
    private int nb_tir; //attribut représent le nombre de tir/balle disponible par l'arme
    public Arme_a_feu(String nom, int degats, int rayon, int nb_tir) {
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
        int wormActif_position_x = wormActif.getPosition().getX();
        int wormActif_position_y = wormActif.getPosition().getY();
        for ( Worm w : ){ //Remplir par une méthode renvoyant l'ensemble des vers dans le monde/terrain
            int w_position_x =  w.getPosition().getX();
            int w_position_y =  w.getPosition().getY();
            boolean condition_x = wormActif_position_x < w_position_x && w_position_x <= wormActif_position_x + this.getRayonExplosion();
            boolean condition_y = wormActif_position_y < w_position_y && w_position_y <= wormActif_position_y + this.getRayonExplosion();
            if ( condition_x || condition_y && w != wormActif && this.getNb_tir() != 0 ){
                w.subirDegats(this.getDegats());// Dégats subis aux vers qui se trouvent dans la portée de l'arme
                setNb_tir(this.getNb_tir() - 1);// Tir donc on décrémente le nombre de tir disponible pour l'arme
            }
        }
    }
}
