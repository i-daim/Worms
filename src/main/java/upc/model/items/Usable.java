package upc.model.items;
import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;

public interface Usable {

    void utiliser(GameWorld gameWorld, Alien alienActif);

    String getNom();
}
