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
    public void saveInventory(Inventory inventaire) throws IOException {

        //Permet de créer le fichier de sauvegarde de type Json
        try (FileWriter file = new FileWriter("saveInventory.json")) {

            //Conversion de l'objet inventaire en json
            // Et implémentation dans le fichier sauvegarde dédié
            gson.toJson(inventaire, file);
        }
    }

    /*Méthode permettant de charger l'objet inventaire*/
    public Inventory loadInventory() throws IOException {

        //Appel du fichier de sauvegarde de l'objet inventaire
        try (FileReader file = new FileReader("saveInventory.json")) {

            //Reconversion de l'objet au format initial
            return gson.fromJson(file, Inventory.class);
        }
    }
}
