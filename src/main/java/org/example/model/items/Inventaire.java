package org.example.model.items;

import java.util.HashMap;
import java.util.Map;

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


}
