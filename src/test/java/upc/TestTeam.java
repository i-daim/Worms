package upc;
import org.junit.jupiter.api.BeforeEach;
import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestTeam {

    private GameWorld world;

    @BeforeEach
    public void setUp() {
        GameManager manager = new GameManager();
        world = manager.getWorld();
    }


    @Test
    public void testConstructeur() {
        Team a = new Team("TEAM A",3);
        assertEquals("TEAM A",a.getName());
        assertEquals(3,a.getAliens().length);
    }

    @Test
    public void testInitializeTeam() {
        Team a = new Team("TEAM A",3);
        a.initializeTeam(world, "",0);
        Alien[] aliens = a.getAliens() ;
        for(Alien w : aliens){
            assertNotNull(w);
        }
    }

    @Test
    public void testEquipeEliminee() {
        Team b = new Team("TEAM B",3);
        b.initializeTeam(world, "",1);

        Alien[] aliens = b.getAliens() ;
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
        Team a = new Team("TEAM A",3);
        assertEquals("TEAM A",a.getName());
    }

    @Test
    public void testGetAliens() {
        Team a = new Team("TEAM A",3);
        assertEquals(a.getAliens(),a.getAliens());
    }
}

