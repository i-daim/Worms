package upc;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.GameWorld;
import upc.model.jeu.GameManager;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;
import org.junit.jupiter.api.Test;

public class TestAlien {
    private Alien alien;
    private Team team;
    private GameWorld world;

    @BeforeEach
    void setup() {
        GameManager gameManager = new GameManager();
        world = gameManager.getWorld();
        team = gameManager.getTeam(0);
        alien = team.getAliens()[0];

        assertNotNull(world);
        assertNotNull(team);
        assertNotNull(alien);
    }
    @Test
    void testEtatInitialAlien() {
        assertTrue(alien.isAlive());
        assertEquals(100, alien.getHealthPoints());
        assertEquals(Alien.State.IDLE, alien.getState());
        assertTrue(alien.getDirection());
    }
    @Test
    void testReceiveDamage() {
        alien.receiveDmg(25);
        assertEquals(75, alien.getHealthPoints());
        assertTrue(alien.isAlive());
    }
    @Test
    void testMortAlien() {
        alien.receiveDmg(200);
        assertEquals(0, alien.getHealthPoints());
        assertFalse(alien.isAlive());
    }


}