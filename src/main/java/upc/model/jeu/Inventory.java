package upc.model.jeu;

import java.util.HashMap;
import java.util.Map;

import upc.model.items.Item;

public class Inventory {
    private final Map<Item.Type, Integer> stockMunitions;

    public Inventory() {
        this.stockMunitions = new HashMap<>();

    }
    public void ajouterMunition(Item.Type w, int nombre) {
        if (nombre <= 0) {
            return;
        }
        stockMunitions.put(w, stockMunitions.getOrDefault(w, 0) + nombre);
    }

    public Map<Item.Type, Integer> getStockMunitions() {
        return stockMunitions;
    }
    public boolean estDisponible(Item.Type w) {
        int amount = stockMunitions.getOrDefault(w, 0);
        return amount > 0 || amount == -1;
    }
    private void initialiserInventaireEquipe(Team team) {
        Inventory inv = team.getInventory();

        inv.ajouterMunition(Item.Type.SHOTGUN, 50);
        inv.ajouterMunition(Item.Type.MINE, 50);
        inv.ajouterMunition(Item.Type.UZI, 50);
        inv.ajouterMunition(Item.Type.GRENADE, 50);
        inv.ajouterMunition(Item.Type.BAZOOKA, 50);
    }
}
