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
import upc.model.save.GameSave;
import upc.model.items.Jetpack;

public class Inventory {


    /** Constante contenant l'ensemble des types d'items autorisés dans l'inventaire */
    private static Set<Item.Type> allowedWeapons = new HashSet<>(Arrays.asList(Item.Type.values()));

    /** Constantes contenant les différentes items présent dans l'inventaire*/
    private static final Item.Type[] ITEMS = {
            Item.Type.SHOTGUN, Item.Type.UZI, Item.Type.BAZOOKA,
            Item.Type.GRENADE, Item.Type.MINE, Item.Type.JETPACK};

    /** Stock des armes et des munitions de l'équipe */
    private final HashMap<Item.Type, Item> weaponStock;
    private final HashMap<Item.Type, Integer> ammoStock;

    /**
     * Constructeur pour {@code Inventory}
     */
    public Inventory() {
        this.weaponStock = new HashMap<>();
        this.ammoStock = new HashMap<>();

        initializeWeapons();
        initializeAmmo();
    }

    /**
     * Constructeur pour {@code Inventory}
     * Visant à charger un inventaire sauvegardé
     * @param inventorySave : objet de sauvegarde pour l'inventaire
     */
    public Inventory(GameSave.InventorySave inventorySave) {
        this.weaponStock =  new HashMap<>();
        this.ammoStock = inventorySave.getAmmoStock();

        initializeWeapons();
    }

    //// Méthodes pour initialiser l'inventaire ////

    /**
     * Méthode permettant d'initialiser tous les types d'armes de l'inventaire
     * En les implémentant dans l'attribut weaponStock
     * Et en équipant une arme
     */
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
    }

    /**
     * Méthode permettant d'initialiser toutes les munitions des armes de l'inventaire
     * En les implémentant dans l'attribut ammoStock
     * Via la méthode addAmmo
     */
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

    /**
     * Méthode permettant d'augmenter le nombre de muntions d'une arme
     * @param weapon : Arme sélectionnée
     * @param qty : Nombre de munitions à ajouter
     */
    public void addAmmo(Item.Type weapon, int qty) {

        //Vérifie que le nombre de munitions utilisées ou présent dans l'arme est invalide
        if (qty <= 0 || getAmmo(weapon) < 0) {
            return;
        }

        //Met à jour le nombre de munitions présent pour l'arme
        ammoStock.put(weapon, getAmmo(weapon) + qty);
    }

    /**
     * Méthode permettant de décrémenter le nombre de muntions d'une arme suite à un tir
     * @param weapon : Arme sélectionnée
     * @param qty : Nombre de munitions utilisées
     */
    public void useAmmo(Item.Type weapon, int qty) {

        //Vérifie que le nombre de munitions utilisées ou présent dans l'arme est invalide
        if (qty <= 0 || getAmmo(weapon) < 0) {
            return;
        }

        //Met à jour le nombre de munitions restant pour l'arme
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

    ////    GETTERS & SETTERS    ////

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
     * Retourne l'item situé à l'indice sélectionnée
     * @param index : indice
     * @return un item {@link Item.Type} si l'indice est valide
     * sinon {@code null}
     */
    public static Item.Type getItemTypeAt(int index) {
        if (index >= 0 && index < ITEMS.length) {
            return ITEMS[index];
        }
        return null;
    }

    /**
     * Modifie l'ensemble des types d'armes reconnus par l'inventaire
     * @param weapons : ensemble de types d'armes
     */
    public static void setAllowedWeapons(Set<Item.Type> weapons) {
        allowedWeapons = weapons;
    }

    /**
     * Retourne l'arme situé à l'indice sélectionnée
     * @param index : indice
     * @return un item {@link Item} si l'indice est valide
     * sinon {@code null}
     */
    public Item getItemByIndex(int index) {
        if (index >= 0 && index < ITEMS.length) {
            return weaponStock.get(ITEMS[index]);
        }
        return null;


    }
}