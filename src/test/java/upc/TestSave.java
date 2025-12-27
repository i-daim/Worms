package upc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.GameManager;
import upc.model.jeu.Inventory;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;
import upc.model.save.SaveAlien;
import upc.model.save.SaveInventory;
import upc.model.save.SaveMap;
import upc.model.save.SaveTeam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSave {

    private Team zlerps;
    private Team glorbs;
    private SaveTeam saveTeam;

    private Inventory zlerpsInventory;
    private Inventory glorbsInventory;
    private SaveInventory saveInventory;

    private Terrain terrain;
    private SaveMap saveMap;

    private GameManager gameManager;

    @BeforeEach
    void setup() {
        zlerps = new Team("Zlerps", 5);
        glorbs = new Team("Glorbs", 5);
        saveTeam = new SaveTeam();

        zlerpsInventory = new Inventory();
        glorbsInventory = new Inventory();
        saveInventory = new SaveInventory();

        gameManager = new GameManager();

        terrain = gameManager.getTerrain();
        saveMap = new SaveMap();
    }

   /*Méthode permettant la création d'alien pour test*/
    private Alien createTestAlien(GameManager gameManager, Team team, int x, int y) {
        Position position = new Position(x, y);
        return new Alien(gameManager.getWorld(), team, position);
    }

    @Test
    /*Méthode test permettant de tester la classe SaveTeam*/
    public void testSaveTeam() throws IOException {
        for (int i =0; i < 5; i++) {
            zlerps.getAliens()[i] = createTestAlien(gameManager, zlerps, i, i + 1);
            glorbs.getAliens()[i] = createTestAlien(gameManager, zlerps, i + 1, i);
        }
        saveTeam.saveTeam(zlerps,glorbs);
        Team[] teams = saveTeam.loadTeam(gameManager);

        assertEquals(teams[0].getName(), zlerps.getName());
        assertEquals(teams[0].getAliens().length, zlerps.getAliens().length);
        assertEquals(teams[0].getActiveAlienIndex(), zlerps.getActiveAlienIndex());


        assertEquals(teams[1].getName(), glorbs.getName());
        assertEquals(teams[1].getAliens().length, glorbs.getAliens().length);
        assertEquals(teams[1].getActiveAlienIndex(), glorbs.getActiveAlienIndex());
    }

    @Test
    /*Méthode permettant de tester la classe SaveInventory*/
    public void testSaveInventory() throws IOException {
        saveInventory.saveInventory(zlerpsInventory, glorbsInventory);
        Inventory[]  inventories = saveInventory.loadInventory();

        assertEquals(inventories[0].getAmmoStock(), zlerpsInventory.getAmmoStock());
        assertEquals(inventories[1].getAmmoStock(), glorbsInventory.getAmmoStock());
    }

    @Test
    /*Méthode permettant de tester la classe SaveMAp*/
    public void testSaveMap() throws IOException {
        saveMap.saveMap(terrain);
        Terrain saveTerrain = saveMap.loadMap();

        //Permet de récupérer l'image de fond de la map
        String cheminBackground = "/maps/" + saveTerrain.getMapName() + "_background.png";
        BufferedImage imageBackground = ImageIO.read(Objects.requireNonNull(
                getClass().getResource(cheminBackground)));
        saveTerrain.setBackground(imageBackground);

        //Permet de récupérer l'image de la map
        String cheminImage = "/maps/" + saveTerrain.getMapName() + ".png";
        BufferedImage image = ImageIO.read(Objects.requireNonNull(
                getClass().getResource(cheminImage)));
        saveTerrain.setImage(image);

        assertEquals(terrain.getMapName(), saveTerrain.getMapName());
        assertEquals(terrain.getWidth(), saveTerrain.getWidth());
        assertEquals(terrain.getHeight(), saveTerrain.getHeight());
        assertArrayEquals(terrain.getCollisions(), saveTerrain.getCollisions());
        assertEquals(terrain.getCollisionsWidth(), saveTerrain.getCollisionsWidth());
        assertEquals(terrain.getCollisionsHeight(), saveTerrain.getCollisionsHeight());

    }

}
