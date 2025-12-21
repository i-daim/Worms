package upc.model.save;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import upc.model.entites.Alien;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SaveTeam {

    //Attribut gson permet la conversion de l'objet en ce type
    private final Gson gson = new Gson();

    /*Méthode permettant la sauvegarde d'un objet équipe*/
    public void saveTeam(Team equipe) throws IOException {

        //Permet de créer le fichier de sauvegarde de type Json
        try (FileWriter file = new FileWriter("Team.json")) {

            //Liste représentant les aliens vivants dans l'équipe
            List<Alien> list = new ArrayList<Alien>();

            //Boucle
            for (Alien alien : equipe.getAliens()) {

                //Si alien vivant ajout dans la liste
                if (alien.isAlive()) {
                    list.add(alien);
                }
            }

            //Conversion de la liste des aliens vivants en json
            // Et implémentation dans le fichier sauvegarde dédié
            gson.toJson(list, file);
        }
    }

    public Team loadTeam() throws IOException {


        try (FileReader file = new FileReader("Team.json")) {

            Type type = new TypeToken<List<Alien>>() {}.getType();

            List<Alien> liste = gson.fromJson(file, type);

            Team equipe = new Team("", liste.size());
            for(int i = 0; i < liste.size(); i++){
                equipe.getAliens()[i] = liste.get(i);
            }

            return equipe;
        }
    }
}
