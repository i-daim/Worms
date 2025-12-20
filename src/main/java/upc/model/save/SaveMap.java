package upc.model.save;

import com.google.gson.Gson;
import upc.model.jeu.Terrain;

import java.io.FileWriter;
import java.io.IOException;

public class SaveMap {

    private final Gson gson = new Gson();

    public void saveMap(Terrain terrain) throws IOException {
        try(FileWriter file = new FileWriter("saveMap.json")) {
            gson.toJson(terrain, file);
        }
    }

}
