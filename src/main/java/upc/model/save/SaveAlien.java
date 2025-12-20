package upc.model.save;

import com.google.gson.Gson;
import upc.model.entites.Alien;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveAlien {

    //Attribut gson permet la conversion de l'objet en ce type
    private final Gson gson = new Gson();

    /*Méthode permettant la sauvegarde d'un objet alien*/
    public void saveAlien(Alien alien) throws IOException {

        //Permet de créer le fichier de sauvegarde de type Json
        try(FileWriter file = new FileWriter("Alien.json")) {

            //Conversion de l'objet alien en json et implémentation dans le fichier sauvegarde dédié
            gson.toJson(alien, file);
        }
    }

    /*Méthode permettant de charger l'objet alien*/
    public Alien loadAlien() throws IOException {

        //Appel de du fichier de sauvegarde de l'bjet alien
        try(FileReader file = new FileReader("Alien.json")) {

            //Reconversion de l'objet au format initial
            return gson.fromJson(file, Alien.class);
        }
    }
}
