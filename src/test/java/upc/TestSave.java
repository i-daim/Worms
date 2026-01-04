package upc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.*;
import upc.model.save.GameSave;


import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSave {

    private Team zlerps;
    private Team glorbs;

    private Inventory zlerpsInventory;
    private Inventory glorbsInventory;

    private Terrain terrain;

    private GameManager gameManager;

    @BeforeEach
    void setup() {
        zlerps = new Team("Zlerps", 5);
        glorbs = new Team("Glorbs", 5);

        zlerpsInventory = new Inventory();
        glorbsInventory = new Inventory();
        Terrain realMap = TerrainGenerator.chooseRandomMap();

        gameManager = new GameManager(realMap, 5);

        terrain = gameManager.getTerrain();
    }

   /*Méthode permettant la création d'alien pour test*/
    private Alien createTestAlien(GameManager gameManager, Team team, int x, int y) {
        Position position = new Position(x, y);
        return new Alien(gameManager.getWorld(), team, position);
    }

    @Test
    /*Méthode test permettant de tester la classe SaveTeam*/
    public void testSaveTeam() {
        for (int i =0; i < 5; i++) {
            zlerps.getAliens()[i] = createTestAlien(gameManager, zlerps, i, i + 1);
            glorbs.getAliens()[i] = createTestAlien(gameManager, zlerps, i + 1, i);
        }

        GameSave.TeamSave teamSaveZlerps = new GameSave.TeamSave(zlerps);
        GameSave.TeamSave teamSaveGlorbs = new GameSave.TeamSave(glorbs);

        Team loadedZlerps = teamSaveZlerps.toTeam(gameManager.getWorld());
        Team loadedGlorbs = teamSaveGlorbs.toTeam(gameManager.getWorld());

        assertEquals(loadedZlerps.getName(), zlerps.getName());
        assertEquals(loadedZlerps.getAliens().length, zlerps.getAliens().length);
        assertEquals(loadedZlerps.getActiveAlienIndex(), zlerps.getActiveAlienIndex());

        assertEquals(loadedGlorbs.getName(), glorbs.getName());
        assertEquals(loadedGlorbs.getAliens().length, glorbs.getAliens().length);
        assertEquals(loadedGlorbs.getActiveAlienIndex(), glorbs.getActiveAlienIndex());
    }

    @Test
    /*Méthode permettant de tester la classe SaveInventory*/
    public void testSaveInventory() {
        GameSave.InventorySave inventorySaveZlerps = new GameSave.InventorySave(zlerpsInventory);
        GameSave.InventorySave inventorySaveGlorbs = new GameSave.InventorySave(glorbsInventory);

        Inventory loadedZlerpsInventory = inventorySaveZlerps.toInventory();
        Inventory loadedGlorbsInventory = inventorySaveGlorbs.toInventory();

        assertEquals(loadedZlerpsInventory.getAmmoStock(), zlerpsInventory.getAmmoStock());
        assertEquals(loadedGlorbsInventory.getAmmoStock(), glorbsInventory.getAmmoStock());
    }

    @Test
    /*Méthode permettant de tester la classe SaveMap*/
    public void testSaveMap() throws IOException {
        GameSave gameSave = new GameSave(gameManager);
        gameSave.save();

        GameSave loadedGameSave = GameSave.load();
        Terrain dummyMap = TerrainGenerator.chooseRandomMap();
        GameManager loadedGameManager = new GameManager(dummyMap, 5);
        loadedGameSave.applyTo(loadedGameManager);

        Terrain loadedTerrain = loadedGameManager.getTerrain();

        assertEquals(terrain.getMapName(), loadedTerrain.getMapName());
        assertEquals(terrain.getWidth(), loadedTerrain.getWidth());
        assertEquals(terrain.getHeight(), loadedTerrain.getHeight());
        assertArrayEquals(terrain.getCollisions(), loadedTerrain.getCollisions());
        assertEquals(terrain.getCollisionsWidth(), loadedTerrain.getCollisionsWidth());
        assertEquals(terrain.getCollisionsHeight(), loadedTerrain.getCollisionsHeight());
    }

}
