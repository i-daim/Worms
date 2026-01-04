package upc.model.items;

public abstract class Weapon implements Item {

    private final String name;
    private final int damage;
    private final int nbOfShots;
    private final int basePower;

    public Weapon(String name, int damage, int nbOfShots, int basePower) {
        this.name = name;
        this.damage = damage;
        this.nbOfShots = nbOfShots;
        this.basePower = basePower;
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
    public int getBasePower() {
        return basePower;
    }
}
