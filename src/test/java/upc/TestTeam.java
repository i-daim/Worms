package upc;
import org.junit.jupiter.api.BeforeEach;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.GameManager;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTeam {

    private GameWorld world;

    @BeforeEach
    public void setUp() {
        upc.model.jeu.Terrain terrainTest = upc.model.jeu.TerrainGenerator.createEmptyTerrain(100, 50);
        GameManager manager = new GameManager(terrainTest, 2);

        world = manager.getWorld();
    }


    @Test
    public void testConstructeur() {
        Team a = new Team("TEAM A", 3);
        assertEquals("TEAM A", a.getName());
        assertEquals(3, a.getAliens().length);
    }

    @Test
    public void testInitializeTeam() {
        Team a = new Team("TEAM A", 3);
        a.initializeTeam(world, "", 0);
        Alien[] aliens = a.getAliens();
        for (Alien w : aliens) {
            assertNotNull(w);
        }
    }

    @Test
    public void testEquipeEliminee() {
        Team b = new Team("TEAM B", 3);
        b.initializeTeam(world, "", 1);

        Alien[] aliens = b.getAliens();
        aliens[0].receiveDmg(100);

        assertFalse(b.isEliminated());
        assertFalse(aliens[0].isAlive());
        assertTrue(aliens[1].isAlive());

        aliens[1].receiveDmg(100);
        aliens[2].receiveDmg(100);

        assertTrue(b.isEliminated());
    }

    @Test
    public void testGetNom() {
        Team a = new Team("TEAM A", 3);
        assertEquals("TEAM A", a.getName());
    }

    @Test
    public void testGetAliens() {
        Team a = new Team("TEAM A", 3);
        assertEquals(a.getAliens(), a.getAliens());
    }

    @Test
    public void testGetActiveAlien() {
        Team a = new Team("TEAM A", 3);
        Alien activeAlien = a.getActiveAlien();
        assertNull(activeAlien);

        a.getAliens()[0] = new Alien(world, a, new Position(0, 1));
        assertNotNull(a.getActiveAlien());
    }

    @Test
    public void testGetAliveAliensNumber() {
        Team a = new Team("TEAM A", 3);
        assertEquals(0, a.getAliveAliensNumber());

        a.getAliens()[0] = new Alien(world, a, new Position(0, 1));
        assertEquals(1, a.getAliveAliensNumber());
    }

    @Test
    public void testIsActiveLastAlien() {
        Team a = new Team("TEAM A", 3);
        assertTrue(a.isActiveLastAlien());

        a.getAliens()[0] = new Alien(world, a, new Position(0, 1));
        a.getAliens()[1] = new Alien(world, a, new Position(1, 0));
        assertFalse(a.isActiveLastAlien());
    }

    @Test
    public void testResetIndex() {
        Team a = new Team("TEAM A", 2);
        int activeAlien = a.getActiveAlienIndex();
        a.resetIndex();
        assertNotEquals(activeAlien, a.getActiveAlienIndex());

        a.getAliens()[0] = new Alien(world, a, new Position(0, 1));
        a.getAliens()[1] = new Alien(world, a, new Position(1, 0));
        a.resetIndex();
        assertEquals(activeAlien, a.getActiveAlienIndex());

    }

    @Test
    public void testSkipToNextAlien() {
        Team a = new Team("TEAM A", 2);
        a.getAliens()[0] = new Alien(world, a, new Position(0, 1));
        a.getAliens()[1] = new Alien(world, a, new Position(1, 0));
        Alien activeAlien = a.getActiveAlien();
        a.skipToNextAlien();
        assertNotEquals(activeAlien,a.getActiveAlien());
    }

    @Test
    public void testToString() {
        Team a = new Team("TEAM A", 2);
        String actual = a.getName() + " (" + a.getAliveAliensNumber() + "/" + a.getAliens().length + " vivants)";
        assertEquals(actual, a.toString());
    }
}

