package upc.model.jeu;

import java.util.HashMap;

import upc.model.items.Bazooka;
import upc.model.items.Grenade;
import upc.model.items.Item;
import upc.model.items.Mine;
import upc.model.items.Shotgun;
import upc.model.items.Uzi;
import upc.model.items.Weapon;
import upc.model.save.InventorySave;

public class Inventory {

    public static final Item.Type[] ITEMS = {
            Item.Type.SHOTGUN, Item.Type.UZI, Item.Type.BAZOOKA,
            Item.Type.GRENADE, Item.Type.MINE};

    // Stock des armes et des munitions de l'équipe
    private final HashMap<Item.Type, Weapon> weaponStock;
    private final HashMap<Item.Type, Integer> ammoStock;
    private Item.Type currentWeaponType;

    // Constructeur
    public Inventory() {
        this.weaponStock = new HashMap<>();
        this.ammoStock = new HashMap<>();

        initializeWeapons();
        initializeAmmo();
    }

    //Constructeur pour charger un inventaire sauvegardé
    public Inventory(InventorySave inventorySave) {
        this.weaponStock =  new HashMap<>();
        this.ammoStock = inventorySave.getAmmoStock();

        initializeWeapons();

    }


////    Méthodes pour initialiser l'inventaire    ////
    private void initializeWeapons() {
        weaponStock.put(Item.Type.SHOTGUN, new Shotgun());
        weaponStock.put(Item.Type.UZI, new Uzi());
        weaponStock.put(Item.Type.BAZOOKA, new Bazooka());
        weaponStock.put(Item.Type.GRENADE, new Grenade());
        weaponStock.put(Item.Type.MINE, new Mine());
        this.currentWeaponType = Item.Type.BAZOOKA;
    }

    private void initializeAmmo() {
        addAmmo(Item.Type.SHOTGUN, 50);
        addAmmo(Item.Type.MINE, 50);
        addAmmo(Item.Type.UZI, 50);
        addAmmo(Item.Type.GRENADE, 50);
        addAmmo(Item.Type.BAZOOKA, 50);
    }


////    Modification des munitions    ////
    public void addAmmo(Item.Type weapon, int qty) {
        if (qty <= 0 || getAmmo(weapon) < 0) {
            return;
        }
        ammoStock.put(weapon, getAmmo(weapon) + qty);
    }

    public void useAmmo(Item.Type weapon, int qty) {
        if (qty <= 0 || getAmmo(weapon) < 0) {
            return;
        }
        ammoStock.put(weapon, Math.max(getAmmo(weapon) - qty, 0));
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

    public Weapon getCurrentWeapon() {
        if (currentWeaponType == null) {
            return null;
        }
        return (Weapon) weaponStock.get(currentWeaponType);
    }

}
