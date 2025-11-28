package org.example.model.items;
import org.example.model.entites.Alien;
import org.example.model.jeu.GameWorld;

public interface Usable {

    void utiliser(GameWorld gameWorld, Alien alienActif);

    String getNom();
}
