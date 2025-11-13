package org.example.model.items;

import java.util.HashMap;
import java.util.Map;
import org.example.model.jeu.Equipe;

public class Inventaire {
    private final Map<Utilisable, Integer> stockMunitions;

    public Inventaire() {
        this.stockMunitions = new HashMap<>();

    }
    public void ajouterMunition(Utilisable u, int nombre) {
        if (nombre <= 0) {
            return;
        }
        stockMunitions.put(u, stockMunitions.getOrDefault(u, 0) + nombre);
    }
    public Map<Utilisable, Integer> getStockMunitions() {
        return stockMunitions;
    }
    public boolean estDisponible(Utilisable u) {
        int qte = stockMunitions.getOrDefault(u, 0);
        return qte > 0 || qte == -1;
    }
    private void initialiserInventaireEquipe(Equipe equipe) {
        Inventaire inv = equipe.getInventaire();

        inv.ajouterMunition(new BombeCollante("Bombe collante"), 50);
        inv.ajouterMunition(new Grenade("Grenade"), 50);
        inv.ajouterMunition(new SainteGrenade("Sainte grenade"), 50);
        inv.ajouterMunition(new Mine("Mine"), 50);
        inv.ajouterMunition(new MineAntichar("Mine anti-char"), 50);
        inv.ajouterMunition(new PiqueVer("Pique vert"), 50);
        inv.ajouterMunition(new Shotgun("Shotgun"), 50);
        inv.ajouterMunition(new Uzi("Uzi"), 50);}

}
