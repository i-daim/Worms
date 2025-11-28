package upc.model.items;

import upc.model.entites.Alien;
import upc.model.entites.Projectile;

public abstract class Weapon implements Usable {
    private String nom;
    private int degats;
    private int rayonExplosion;

    public Weapon(String nom, int degats, int rayonExplosion) {
        this.nom = nom;
        this.degats = degats;
        this.rayonExplosion = rayonExplosion;
    }

    public String getNom() {
        return nom;
    }
    public int getDegats() {
        return degats;
    }
    public int getRayonExplosion() {
        return rayonExplosion;
    }

    public abstract double getInitialVelocity();
    public abstract boolean isAffectedByWind();
    public Projectile createProjectile(Alien shooter) {
        int x = shooter.getPosition().getX();
        int y = shooter.getPosition().getY();
        return new Projectile(this, shooter, x, y);
    }



}
