package upc.model.save;

import com.google.gson.Gson;
import upc.model.jeu.Terrain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class SaveMap {

    //Attribut gson permet la conversion de l'objet en ce type
    private final Gson gson = new Gson();

    /*Méthode permettant la sauvegarde d'un objet terrain*/
    public void saveMap(Terrain terrain) throws IOException {

        //Ajout d'un répertoire pour fichiers de sauvegarde
        File saveDir = new  File("saveData");
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        //Permet de créer le fichier de sauvegarde de type Json
        try (FileWriter file = new FileWriter("saveData/saveMap.json")) {

            //Conversion de l'objet terrain en json
            // Et implémentation dans le fichier sauvegarde dédié
            gson.toJson(terrain, file);
        }
    }

    /*Méthode permettant de charger l'objet Terrain*/
    public Terrain loadMap() throws IOException {

        //Appel du fichier de sauvegarde de l'objet terrain
        try (FileReader file = new FileReader("saveData/saveMap.json")) {

            //Reconversion de l'objet au format initial
            return gson.fromJson(file, Terrain.class);
        }
    }

    /*Méthode permettant de récuperér les images de la map*/
    public void loadImage(Terrain terrain) throws IOException {

        //Permet de récupérer l'image de fond de la map
        String cheminBackground = "/maps/" + terrain.getMapName() + "_background.png";
        BufferedImage imageBackground = ImageIO.read(Objects.requireNonNull(
                getClass().getResource(cheminBackground)));
        terrain.setBackground(imageBackground);

        //Permet de récupérer l'image de la map
        String cheminImage = "/maps/" + terrain.getMapName() + ".png";
        BufferedImage image = ImageIO.read(Objects.requireNonNull(
                getClass().getResource(cheminImage)));
        terrain.setImage(image);

    }

}
