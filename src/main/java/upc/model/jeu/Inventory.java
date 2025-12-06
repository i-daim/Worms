package upc.model.jeu;

import java.util.HashMap;

import upc.model.items.Bazooka;
import upc.model.items.Grenade;
import upc.model.items.Item;
import upc.model.items.Mine;
import upc.model.items.Shotgun;
import upc.model.items.Uzi;
import upc.model.items.Weapon;

public class Inventory {
    // Stock des armes et des munitions de l'équipe
    private final HashMap<Item.Type, Weapon> weaponStock;
    private final HashMap<Item.Type, Integer> ammoStock;

    // Constructeur
    public Inventory() {
        this.weaponStock = new HashMap<>();
        this.ammoStock = new HashMap<>();

        initializeWeapons();
        initializeAmmo();
    }


////    Méthodes pour initialiser l'inventaire    ////
    private void initializeWeapons() {
        weaponStock.put(Item.Type.SHOTGUN, new Shotgun());
        weaponStock.put(Item.Type.UZI, new Uzi());
        weaponStock.put(Item.Type.BAZOOKA, new Bazooka());
        weaponStock.put(Item.Type.GRENADE, new Grenade());
        weaponStock.put(Item.Type.MINE, new Mine());
    }

    private void initializeAmmo() {
        addAmmo(Item.Type.SHOTGUN, 50);
        addAmmo(Item.Type.MINE, 50);
        addAmmo(Item.Type.UZI, 50);
        addAmmo(Item.Type.GRENADE, 50);
        addAmmo(Item.Type.BAZOOKA, 50);
    }


////    Modification des munitions    ////
    public void addAmmo(Item.Type w, int qty) {
        if (qty <= 0) {
            return;
        }
        ammoStock.put(w, ammoStock.getOrDefault(w, 0) + qty);
    }

    public void useAmmo(Item.Type w, int qty) {
        if (qty <= 0) {
            return;
        }
        ammoStock.put(w, Math.max(getAmmo(w) - qty, 0));
    }


////    Autres méthodes    ////
    public boolean isAvailable(Item.Type w) {
        int amount = ammoStock.getOrDefault(w, 0);
        return amount > 0 || amount == -1;
    }


////    Getters    ////
    public HashMap<Item.Type, Integer> getAmmoStock() {
        return ammoStock;
    }

    public Weapon getWeapon(Item.Type w) {
        return weaponStock.get(w);
    }

    public int getAmmo(Item.Type w) {
        return ammoStock.getOrDefault(w, 0);
    }

}
