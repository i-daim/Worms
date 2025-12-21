package upc.model.save;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import upc.model.entites.Alien;
import upc.model.jeu.Team;

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

    /*Méthode permettant de charger l'objet Team*/
    public Team loadTeam(Team equipe) throws IOException {

        //Permet de lire le fichier de sauvegarde de type Json
        try (FileReader file = new FileReader("Team.json")) {

            //Permet de convertir la list<Alien> dans un format compréhensible par Gson
            Type type = new TypeToken<List<Alien>>() { }.getType();

            //Liste des aliens vivants de l'équipe
            List<Alien> liste = gson.fromJson(file, type);

            //Recréation de l'équipe sauvegardé
            Team saveequipe = new Team(equipe.getName(), liste.size());
            for (int i = 0; i < liste.size(); i++) {
                saveequipe.getAliens()[i] = liste.get(i);
            }

            //Renvoie l'équipe sauvegardé
            return saveequipe;
        }
    }
}
