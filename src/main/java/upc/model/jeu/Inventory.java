package upc.model.jeu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

import upc.model.items.Bazooka;
import upc.model.items.Grenade;
import upc.model.items.Item;
import upc.model.items.Jetpack;
import upc.model.items.Mine;
import upc.model.items.Shotgun;
import upc.model.items.Uzi;
import upc.model.items.Weapon;

public class Inventory {
    public static Set<Item.Type> ALLOWED_WEAPONS = new HashSet<>
            (Arrays.asList(Item.Type.values()));
    public static final Item.Type[] ITEMS = {
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

    //// Méthode pour initialiser l'inventaire ////
    private void initializeWeapons() {
        if (ALLOWED_WEAPONS.contains(Item.Type.SHOTGUN)) weaponStock.put(Item.Type.SHOTGUN, new Shotgun());
        if (ALLOWED_WEAPONS.contains(Item.Type.UZI)) weaponStock.put(Item.Type.UZI, new Uzi());
        if (ALLOWED_WEAPONS.contains(Item.Type.BAZOOKA)) weaponStock.put(Item.Type.BAZOOKA, new Bazooka());
        if (ALLOWED_WEAPONS.contains(Item.Type.GRENADE)) weaponStock.put(Item.Type.GRENADE, new Grenade());
        if (ALLOWED_WEAPONS.contains(Item.Type.MINE)) weaponStock.put(Item.Type.MINE, new Mine());

        for (Item.Type t : ITEMS) {
            if (weaponStock.containsKey(t)) {
                this.currentWeaponType = t;
                break;
            }
        }
    }

    private void initializeAmmo() {
        addAmmo(Item.Type.SHOTGUN, 50);
        addAmmo(Item.Type.MINE, 50);
        addAmmo(Item.Type.UZI, 50);
        addAmmo(Item.Type.GRENADE, 50);
        addAmmo(Item.Type.BAZOOKA, 50);
        addAmmo(Item.Type.JETPACK, 250);
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
}