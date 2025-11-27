package org.example.model.items;

import org.example.model.entites.Projectile;
import org.example.model.entites.Worm;

public abstract class Arme implements Utilisable {
    private String nom;
    private int degats;
    private int rayonExplosion;

    public Arme(String nom, int degats, int rayonExplosion) {
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
    public abstract Projectile createProjectile(Worm shooter);



}
