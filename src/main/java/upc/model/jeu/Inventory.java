package upc.model.jeu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

import upc.model.items.Bazooka;
import upc.model.items.Grenade;
import upc.model.items.Item;
import upc.model.items.Mine;
import upc.model.items.Shotgun;
import upc.model.items.Uzi;
import upc.model.items.Weapon;
import upc.model.save.GameSave;

public class Inventory {
    private static Set<Item.Type> allowedWeapons = new HashSet<>(Arrays.asList(Item.Type.values()));
    private static final Item.Type[] ITEMS = {
            Item.Type.SHOTGUN, Item.Type.UZI, Item.Type.BAZOOKA,
            Item.Type.GRENADE, Item.Type.MINE, Item.Type.JETPACK};

    // Stock des armes et des munitions de l'équipe
    private final HashMap<Item.Type, Item> weaponStock;
    private final HashMap<Item.Type, Integer> ammoStock;
    private Item.Type currentWeaponType;

    // Constructeur
    public Inventory() {
        this.weaponStock = new HashMap<>();
        this.ammoStock = new HashMap<>();

        initializeWeapons();
        initializeAmmo();
    }
    public static Item.Type getItemTypeAt(int index) {
        if (index >= 0 && index < ITEMS.length) {
            return ITEMS[index];
        }
        return null;
    }
    //Constructeur pour charger un inventaire sauvegardé
    public Inventory(GameSave.InventorySave inventorySave) {
        this.weaponStock =  new HashMap<>();
        this.ammoStock = inventorySave.getAmmoStock();

        initializeWeapons();
    }

    //// Méthode pour initialiser l'inventaire ////
    private void initializeWeapons() {
        if (allowedWeapons.contains(Item.Type.SHOTGUN)) {
            weaponStock.put(Item.Type.SHOTGUN, new Shotgun());
        }
        if (allowedWeapons.contains(Item.Type.UZI)) {
            weaponStock.put(Item.Type.UZI, new Uzi());
        }
        if (allowedWeapons.contains(Item.Type.BAZOOKA)) {
            weaponStock.put(Item.Type.BAZOOKA, new Bazooka());
        }
        if (allowedWeapons.contains(Item.Type.GRENADE)) {
            weaponStock.put(Item.Type.GRENADE, new Grenade());
        }
        if (allowedWeapons.contains(Item.Type.MINE)) {
            weaponStock.put(Item.Type.MINE, new Mine());
        }
        if (allowedWeapons.contains(Item.Type.JETPACK)) {
            weaponStock.put(Item.Type.JETPACK, new Jetpack());
        }

        for (Item.Type t : ITEMS) {
            if (weaponStock.containsKey(t)) {
                this.currentWeaponType = t;
                break;
            }
        }
    }

    private void initializeAmmo() {

        if (allowedWeapons.contains(Item.Type.SHOTGUN)) {
            addAmmo(Item.Type.SHOTGUN, 50);
        }
        if (allowedWeapons.contains(Item.Type.MINE)) {
            addAmmo(Item.Type.MINE, 50);
        }
        if (allowedWeapons.contains(Item.Type.UZI)) {
            addAmmo(Item.Type.UZI, 500);
        }
        if (allowedWeapons.contains(Item.Type.GRENADE)) {
            addAmmo(Item.Type.GRENADE, 50);
        }
        if (allowedWeapons.contains(Item.Type.BAZOOKA)) {
            addAmmo(Item.Type.BAZOOKA, 50);
        }
        if (allowedWeapons.contains(Item.Type.JETPACK)) {
            addAmmo(Item.Type.JETPACK, 200);
        }

    }

    //// Modifications des munitions ////
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

    //// Autres méthodes ////
    public boolean isAvailable(Item.Type w) {
        int amount = ammoStock.getOrDefault(w, 0);
        return amount > 0 || amount == -1;
    }

    public HashMap<Item.Type, Integer> getAmmoStock() {
        return ammoStock;
    }

    public Item getItem(Item.Type w) {
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
    public static void setAllowedWeapons(Set<Item.Type> weapons) {
        allowedWeapons = weapons;
    }
    public Item getItemByIndex(int index) {
        if (index >= 0 && index < ITEMS.length) {
            return weaponStock.get(ITEMS[index]);
        }
        return null;
    }
}