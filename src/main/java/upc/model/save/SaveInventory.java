package upc.model.save;

import com.google.gson.Gson;
import upc.model.jeu.Inventory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveInventory {

    //Attribut gson permet la conversion de l'objet en ce type
    private final Gson gson = new Gson();

    /*Méthode permettant la sauvegarde d'un objet inventaire*/
    public void saveInventory(Inventory zlinventory, Inventory glinventory) throws IOException {

        //Permet de créer le fichier de sauvegarde de type Json
        try (FileWriter file = new FileWriter("ZlerpsInventory.json")) {

            //Conversion de l'objet inventaire pour l'équipe zlerps en json
            // Et implémentation dans le fichier sauvegarde dédié
            gson.toJson(zlinventory, file);
        }

        //Permet de créer le fichier de sauvegarde de type Json
        try (FileWriter file = new FileWriter("GlorbsInventory.json")) {

            //Conversion de l'objet inventaire pour l'équipe glorbs en json
            // Et implémentation dans le fichier sauvegarde dédié
            gson.toJson(glinventory, file);
        }
    }

    /*Méthode permettant de charger l'objet inventaire*/
    public Inventory[] loadInventory() throws IOException {

        //Tableau contenant les inventaires de chaque équipe
        Inventory[] equipesinventaire = new Inventory[2];

        //Appel du fichier de sauvegarde de l'objet inventaire zlerps
        try (FileReader file = new FileReader("ZlerpsInventory.json")) {

            //Ajout inventaire sauvegardé de l'équipe zlerps
            equipesinventaire[0] = gson.fromJson(file, Inventory.class);
        }

        //Appel du fichier de sauvegarde de l'objet inventaire glorbs
        try (FileReader file = new FileReader("GlorbsInventory.json")) {

            //Ajout inventaire sauvegardé de l'équipe glorbs
            equipesinventaire[1] = gson.fromJson(file, Inventory.class);
        }

        //Renvoie le tableau des inventaires
        return equipesinventaire;
    }
}
