package upc.model.items;
import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;

public class Bazooka extends Weapon {
    public Bazooka() {
        super("Bazooka", 45, 3, 70);
    }
    @Override
    public void useItem(GameWorld gameWorld, Alien shooter) {
    }

}
