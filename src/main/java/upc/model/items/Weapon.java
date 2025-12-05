package upc.model.items;

public abstract class Weapon implements Item {

    private String name;
    private int damage;
    private int nbOfShots;
    private int puissanceTir;

    public Weapon(String name, int damage, int nbOfShots, int puissanceTir) {
        this.name = name;
        this.damage = damage;
        this.nbOfShots = nbOfShots;
        this.puissanceTir = puissanceTir;
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
    public int getPuissanceTir() {
        return puissanceTir;
    }
}
