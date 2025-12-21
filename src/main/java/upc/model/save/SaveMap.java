package upc.model.save;

import com.google.gson.Gson;
import upc.model.jeu.Terrain;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveMap {

    //Attribut gson permet la conversion de l'objet en ce type
    private final Gson gson = new Gson();

    /*Méthode permettant la sauvegarde d'un objet terrain*/
    public void saveMap(Terrain terrain) throws IOException {

        //Permet de créer le fichier de sauvegarde de type Json
        try(FileWriter file = new FileWriter("saveMap.json")) {

            //Conversion de l'objet terrain en json et implémentation dans le fichier sauvegarde dédié
            gson.toJson(terrain, file);
        }
    }

    /*Méthode permettant de charger l'objet Terrain*/
    public Terrain loadMap() throws IOException {

        //Appel du fichier de sauvegarde de l'objet terrain
        try(FileReader file = new FileReader("saveMap.json")) {

            //Reconversion de l'objet au format initial
            return gson.fromJson(file, Terrain.class);
        }
    }

}
