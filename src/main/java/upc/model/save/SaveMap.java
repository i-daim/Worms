package upc.model.save;

import com.google.gson.Gson;
import upc.model.jeu.Terrain;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveMap {

    private final Gson gson = new Gson();

    public void saveMap(Terrain terrain) throws IOException {
        try(FileWriter file = new FileWriter("saveMap.json")) {
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
