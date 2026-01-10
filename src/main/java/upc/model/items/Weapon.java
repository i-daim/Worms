package upc.model.items;

public abstract class Weapon implements Item {

    /**Attributs représentants le nom, les dégâts, le nombre de tir et la puissance de base d'une arme*/
    private final String name;
    private final int damage;
    private final int nbOfShots;
    private final int basePower;

    /**
     * Constructeur pour {@code Weapon}
     * @param name : Nom de l'arme
     * @param damage : Dégâts de l'arme
     * @param nbOfShots : Le nombre de tir de l'arme
     * @param  basePower : La puissance de base de l'arme
     */
    public Weapon(String name, int damage, int nbOfShots, int basePower) {
        this.name = name;
        this.damage = damage;
        this.nbOfShots = nbOfShots;
        this.basePower = basePower;
    }

////    GETTERS    ////


    /**
     * Retourne le nom de l'arme
     * @return l'attribut name
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne les dégâts de l'arme
     * @return l'attribut damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Retourne le nombre de tirs de l'arme
     * @return l'attribut nbOfShots
     */
    public int getNbOfShots() {
        return nbOfShots;
    }

    /**
     * Retourne le puissance de base de l'arme
     * @return l'attribut basePower
     */
    public int getBasePower() {
        return basePower;
    }
}
