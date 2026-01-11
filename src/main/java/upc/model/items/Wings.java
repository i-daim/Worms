package upc.model.items;

import upc.model.entites.Alien;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameWorld;

public class Wings extends Tool {

    public Wings() {
        super("Wings");
    }

    @Override
    public Projectile useItem(GameWorld gameWorld, Alien alienActif) {
        return null;
    }

    @Override
    public Type getType() {
        return Type.WINGS;
    }

}