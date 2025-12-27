package upc.model.save;

import com.google.gson.Gson;
import upc.model.jeu.Inventory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveInventory {

    //Attribut gson permet la conversion de l'objet en ce type
    private final Gson gson = new Gson();

    /*Méthode permettant la sauvegarde d'un objet inventaire*/
    public void saveInventory(Inventory zlinventory, Inventory glinventory) throws IOException {

        //Ajout d'un répertoire pour fichiers de sauvegarde
        File saveDir = new  File("saveData");
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        //Permet de créer le fichier de sauvegarde de type Json
        try (FileWriter file = new FileWriter("saveData/ZlerpsInventory.json")) {

            //Crée un objet de sauvegarde minimal pour l'inventaire des zlerps
            InventorySave zlinventorySave = new InventorySave(zlinventory);

            //Conversion de l'objet inventaire pour l'équipe zlerps en json
            // Et implémentation dans le fichier sauvegarde dédié
            gson.toJson(zlinventorySave, file);
        }

        //Permet de créer le fichier de sauvegarde de type Json
        try (FileWriter file = new FileWriter("saveData/GlorbsInventory.json")) {

            //Crée un objet de sauvegarde minimal pour l'inventaire des glorbs
            InventorySave glinventorySave = new InventorySave(glinventory);

            //Conversion de l'objet inventaire pour l'équipe glorbs en json
            // Et implémentation dans le fichier sauvegarde dédié
            gson.toJson(glinventorySave, file);

        }
    }

    /*Méthode permettant de charger l'objet inventaire*/
    public Inventory[] loadInventory() throws IOException {

        //Tableau contenant les inventaires de chaque équipe
        Inventory[] equipesinventaire = new Inventory[2];

        //Appel du fichier de sauvegarde de l'objet inventaire zlerps
        try (FileReader file = new FileReader("saveData/ZlerpsInventory.json")) {

            //Recrée un inventaire pour l'équipe zlerps
            Inventory zlerpsInventory = new Inventory(gson.fromJson(file, InventorySave.class));

            //Ajout inventaire sauvegardé de l'équipe zlerps
            equipesinventaire[0] = zlerpsInventory;
        }

        //Appel du fichier de sauvegarde de l'objet inventaire glorbs
        try (FileReader file = new FileReader("saveData/GlorbsInventory.json")) {

            //Recrée un inventaire pour l'équipe glorbs
            Inventory glorbsInventory = new Inventory(gson.fromJson(file, InventorySave.class));

            //Ajout inventaire sauvegardé de l'équipe glorbs
            equipesinventaire[1] = glorbsInventory;
        }

        //Renvoie le tableau des inventaires
        return equipesinventaire;
    }
}
