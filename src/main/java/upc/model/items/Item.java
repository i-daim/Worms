package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameWorld;

public interface Item {

    enum Type {
        SHOTGUN,
        UZI,
        BAZOOKA,
        GRENADE,
        MINE,
        JETPACK
    }

    Projectile useItem(GameWorld gameWorld, Alien alienActif);

    String getName();

    Type getType();
}
