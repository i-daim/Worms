package upc.model.save;

import com.google.gson.Gson;
import upc.model.entites.Alien;
import upc.model.jeu.Team;

import java.io.FileWriter;
import java.io.IOException;

public class SaveTeam {

    private final Gson gson = new Gson();

    public void saveTeam(Team equipe) throws IOException {
        try(FileWriter file = new FileWriter("Team.json")) {
            for (Alien alien : equipe.getAliens()) {
                if (alien.isAlive()) {
                    file.write(gson.toJson(alien));
                }
            }
        }
    }
}
