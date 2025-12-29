package upc;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;
import upc.model.jeu.GameManager;
import upc.model.jeu.Team;
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

    @Test
    void testMoveRight(){
        alien.moveRight() ;
        assertEquals(Alien.State.WALK, alien.getState());
        assertTrue(alien.getDirection());
    }

    @Test
    void testMoveLeft() {
        alien.moveLeft();
        assertEquals(Alien.State.WALK, alien.getState());
        assertFalse(alien.getDirection());
    }
    @Test
    void testIdle() {
        alien.idle();
        assertEquals(Alien.State.IDLE, alien.getState());
    }
    @Test
    void testJump() {
        alien.jump();
        assertEquals(Alien.State.JUMP, alien.getState());
    }
    @Test
    void testAimAngle() {
        alien.setAimAngle(30);
        assertEquals(30, alien.getAimAngle());
    }
    @Test
    void testShootPower() {
        alien.setShootPower(90);
        assertEquals(90, alien.getShootPower());
    }
    @Test
    void testSelectedItemIndex() {
        alien.setSelectedItemIndex(0);
        assertEquals(0, alien.getSelectedItemIndex());
    }
    @Test
    void testGetTeam() {
        assertEquals(team, alien.getTeam());
    }






}