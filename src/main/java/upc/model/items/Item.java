package upc.model.items;
import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;

public interface Usable {
public interface Item {

    void useItem(GameWorld gameWorld, Alien alienActif);

    String getName();
}
