package org.example.model.items;
import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;

public interface Utilisable {

    void utiliser(Monde monde, Worm wormActif);

    String getNom();
}
