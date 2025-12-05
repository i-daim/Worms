package upc.model.items;
import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;

public interface Item {

    enum Type {
        SHOTGUN,
        UZI,
        BAZOOKA,
        GRENADE,
        MINE
    }

    void useItem(GameWorld gameWorld, Alien alienActif);
}
