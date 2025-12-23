package upc.model.save;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import upc.model.entites.Alien;
import upc.model.jeu.AlienSave;
import upc.model.jeu.GameManager;
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
    public void saveTeam(Team zlerps, Team glorbs) throws IOException {

        //Permet de créer le fichier de sauvegarde de type Json
        //Pour l'équipe zlerps
        try (FileWriter file = new FileWriter("zlerps.json")) {

            //Liste représentant les aliens vivants dans l'équipe zlerps
            List<AlienSave> list = new ArrayList<AlienSave>();

            //Boucle
            for (Alien alien : zlerps.getAliens()) {

                if (alien.isAlive()) {
                    AlienSave alienSave = new AlienSave(alien);
                    list.add(alienSave);
                }

            }

            //Conversion de la liste des aliens vivants en json
            // Et implémentation dans le fichier sauvegarde dédié
            gson.toJson(list, file);
        }

        //Permet de créer le fichier de sauvegarde de type Json
        //Pour l'équipe glorps
        try (FileWriter file = new FileWriter("glorbs.json")) {

            //Liste représentant les aliens vivants dans l'équipe glorbs
            List<AlienSave> list = new ArrayList<AlienSave>();

            //Boucle
            for (Alien alien : glorbs.getAliens()) {

                //Si alien vivant ajout dans la liste
                if (alien.isAlive()) {
                    AlienSave alienSave = new AlienSave(alien);
                    list.add(alienSave);
                }
            }

            //Conversion de la liste des aliens vivants en json
            // Et implémentation dans le fichier sauvegarde dédié
            gson.toJson(list, file);
        }


    }

    /*Méthode permettant de charger l'objet Team*/
    public Team[] loadTeam(GameManager gameManager) throws IOException {

        Team[] teams = new Team[2];

        //Permet de lire le fichier de sauvegarde de type Json
        try (FileReader file = new FileReader("zlerps.json")) {

            //Permet de convertir la list<Alien> dans un format compréhensible par Gson
            Type type = new TypeToken<List<AlienSave>>() { }.getType();

            //Liste des aliens vivants de l'équipe zlerps
            List<AlienSave> liste = gson.fromJson(file, type);

            //Recréation de l'équipe sauvegardé zleprs
            Team savezlerps = new Team("Zlerps", liste.size());
            for (int i = 0; i < liste.size(); i++) {
                Alien zlerps = new Alien(liste.get(i), gameManager.getWorld(), savezlerps);
                savezlerps.getAliens()[i] = zlerps;
            }

            teams[0] = savezlerps;

        }

        //Permet de lire le fichier de sauvegarde de type Json
        try (FileReader file = new FileReader("glorbs.json")) {

            //Permet de convertir la list<Alien> dans un format compréhensible par Gson
            Type type = new TypeToken<List<AlienSave>>() { }.getType();

            //Liste des aliens vivants de l'équipe glorbs
            List<AlienSave> liste = gson.fromJson(file, type);

            //Recréation de l'équipe sauvegardé glorbs
            Team saveglorbs = new Team("Glorbs", liste.size());
            for (int i = 0; i < liste.size(); i++) {
                Alien glorbs = new Alien(liste.get(i), gameManager.getWorld(), saveglorbs);
                saveglorbs.getAliens()[i] = glorbs;
            }

            teams[1] = saveglorbs;

        }

        //renvoie les équipes sauvegardés
        return teams;
    }
}
