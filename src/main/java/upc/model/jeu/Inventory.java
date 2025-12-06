package upc.model.jeu;

import java.util.HashMap;
import java.util.Map;

import upc.model.items.Bazooka;
import upc.model.items.Grenade;
import upc.model.items.Item;
import upc.model.items.Mine;
import upc.model.items.Shotgun;
import upc.model.items.Uzi;
import upc.model.items.Weapon;

public class Inventory {
    private final HashMap<Item.Type, Weapon> weaponStock;
    private final HashMap<Item.Type, Integer> ammoStock;

    public Inventory() {
        this.weaponStock = new HashMap<>();
        this.ammoStock = new HashMap<>();

        initializeWeapons();
        initializeAmmo();
    }
    public void addAmmo(Item.Type w, int qty) {
        if (qty <= 0) {
            return;
        }
        ammoStock.put(w, ammoStock.getOrDefault(w, 0) + qty);
    }

    public Map<Item.Type, Integer> getAmmoStock() {
        return ammoStock;
    }
    public boolean isAvailable(Item.Type w) {
        int amount = ammoStock.getOrDefault(w, 0);
        return amount > 0 || amount == -1;
    }
    private void initializeAmmo() {
        addAmmo(Item.Type.SHOTGUN, 50);
        addAmmo(Item.Type.MINE, 50);
        addAmmo(Item.Type.UZI, 50);
        addAmmo(Item.Type.GRENADE, 50);
        addAmmo(Item.Type.BAZOOKA, 50);
    }

    private void initializeWeapons() {
        weaponStock.put(Item.Type.SHOTGUN, new Shotgun());
        weaponStock.put(Item.Type.UZI, new Uzi());
        weaponStock.put(Item.Type.BAZOOKA, new Bazooka());
        weaponStock.put(Item.Type.GRENADE, new Grenade());
        weaponStock.put(Item.Type.MINE, new Mine());
    }

    public Weapon getWeapon(Item.Type w) {
        return weaponStock.get(w);
    }

}
