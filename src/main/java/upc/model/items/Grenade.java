package upc.model.items;

public class Grenade extends Weapon {
    public static final int DEGATS = 45;
    public static final int RAYON = 5;

    public Grenade(String nom) {
        super(nom, DEGATS, RAYON, 1);
    }
}
