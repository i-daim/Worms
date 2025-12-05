package upc.model.items;

public abstract class Weapon implements Item {

    private String name;
    private int damage;
    private int nbOfShots;

    public Weapon(String name, int damage, int nbOfShots) {
        this.name = name;
        this.damage = damage;
        this.nbOfShots = nbOfShots;
    }

////    GETTERS    ////
    public String getName() {
        return name;
    }
    public int getDamage() {
        return damage;
    }
    public int getNbOfShots() {
        return nbOfShots;
    }
}
