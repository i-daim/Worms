package upc.model.save;

import upc.model.items.Item;
import upc.model.jeu.Inventory;

import java.util.HashMap;

/*Classe permet de sauvegarder les informations minimal d'un inventaire*/
public class InventorySave {

    //Attribut minimal de sauvegarde de ALien
    private final HashMap<Item.Type, Integer> ammoStock;


    //Constructeur
    public InventorySave(Inventory inventory) {
        this.ammoStock = inventory.getAmmoStock();
    }

    //Getters

    public HashMap<Item.Type, Integer> getAmmoStock() {
        return ammoStock;
    }

}
