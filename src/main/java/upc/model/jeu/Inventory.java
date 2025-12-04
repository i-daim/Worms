package upc.model.jeu;

import java.util.HashMap;
import java.util.Map;

import upc.model.items.Grenade;
import upc.model.items.Mine;
import upc.model.items.Shotgun;
import upc.model.items.Usable;
import upc.model.items.Uzi;

public class Inventory {
    private final Map<Usable, Integer> stockMunitions;

    public Inventory() {
        this.stockMunitions = new HashMap<>();

    }
    public void ajouterMunition(Usable u, int nombre) {
        if (nombre <= 0) {
            return;
        }
        stockMunitions.put(u, stockMunitions.getOrDefault(u, 0) + nombre);
    }
    public Map<Usable, Integer> getStockMunitions() {
        return stockMunitions;
    }
    public boolean estDisponible(Usable u) {
        int qte = stockMunitions.getOrDefault(u, 0);
        return qte > 0 || qte == -1;
    }
    private void initialiserInventaireEquipe(Team team) {
        Inventory inv = team.getInventory();

        inv.ajouterMunition(new Grenade(), 50);
        inv.ajouterMunition(new Mine(), 50);
        inv.ajouterMunition(new Shotgun(), 50);
        inv.ajouterMunition(new Uzi(), 50);
    }
}
