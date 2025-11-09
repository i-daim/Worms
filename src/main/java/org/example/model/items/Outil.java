package org.example.model.items;

public abstract class Outil implements Utilisable {
    private String nom;

    public Outil(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}
