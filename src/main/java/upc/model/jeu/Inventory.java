package upc.model.jeu;

import java.util.HashMap;

import upc.model.items.Bazooka;
import upc.model.items.Grenade;
import upc.model.items.Item;
import upc.model.items.Jetpack;
import upc.model.items.Mine;
import upc.model.items.Shotgun;
import upc.model.items.Uzi;
import upc.model.items.Weapon;
import upc.model.save.GameSave;

public class Inventory {

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

    //Constructeur pour charger un inventaire sauvegardé
    public Inventory(GameSave.InventorySave inventorySave) {
        this.weaponStock =  new HashMap<>();
        this.ammoStock = inventorySave.getAmmoStock();

        initializeWeapons();
    }


////    Méthodes pour initialiser l'inventaire    ////
    //// Méthode pour initialiser l'inventaire ////
    private void initializeWeapons() {
        weaponStock.put(Item.Type.SHOTGUN, new Shotgun());
        weaponStock.put(Item.Type.UZI, new Uzi());
        weaponStock.put(Item.Type.BAZOOKA, new Bazooka());
        weaponStock.put(Item.Type.GRENADE, new Grenade());
        weaponStock.put(Item.Type.MINE, new Mine());
        weaponStock.put(Item.Type.JETPACK, new Jetpack());
        this.currentWeaponType = Item.Type.BAZOOKA;
    }

    private void initializeAmmo() {
        addAmmo(Item.Type.SHOTGUN, 50);
        addAmmo(Item.Type.MINE, 50);
        addAmmo(Item.Type.UZI, 500);
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

    /**
     * Méthode permettant de savoir si une arme est utilisable ou non
     * @param w : Item représentant une arme
     * @return boolean {@code boolean}
     * si le nombre de munitions de l'item donnée en paramètre est valide
     */
    public boolean isAvailable(Item.Type w) {
        int amount = ammoStock.getOrDefault(w, 0);
        return amount > 0 || amount == -1;
    }

    /// Getters ///

    /**
     * Retourne la liste des différentes munitions utilisées par les armes
     * @return L'attribut ammoStock
     */
    public HashMap<Item.Type, Integer> getAmmoStock() {
        return ammoStock;
    }

    /**
     * Retourne l'item présent dans la liste représentant l'ensemble des armes de l'inventaire
     * @param w : Un item donné
     * @return l'item présent dans l'attribut weaponStock
     */
    public Item getItem(Item.Type w) {
        return weaponStock.get(w);
    }

    /**
     * Retourne le nombre de munitions de l'item mis en paramètre
     * @param w : Un item donné
     * @return le nombre de munitions via l'attribut ammoStock
     */
    public int getAmmo(Item.Type w) {
        return ammoStock.getOrDefault(w, 0);
    }

    /**
     * Méthode permettant de retourner l'arme équipé
     * @return l'objet {@link Weapon} représentant l'arme équipée,
     * ou {@code null} si aucune arme n'est équipée
     */
    public Weapon getCurrentWeapon() {
        if (currentWeaponType == null) {
            return null;
        }

        //Retourne l'arme équipé en l'appelant depuis l'attribut weaponStock
        //Et en castant le résultat de sortie en weapon
        return (Weapon) weaponStock.get(currentWeaponType);
    }
}