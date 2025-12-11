package upc.model.items;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Projectile;

public abstract class Weapon implements Item {

    private String name;
    private int damage;
    private int nbOfShots;
    private int speed;

    public Weapon(String name, int damage, int nbOfShots, int puissanceTir) {
        this.name = name;
        this.damage = damage;
        this.nbOfShots = nbOfShots;
        this.speed = puissanceTir;
    }
    public abstract Projectile createProjectile(Alien shooter, Position startPosition, int angleDegrees, int powerPercent);

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
    public int getSpeed() {
        return speed;
    }
}
