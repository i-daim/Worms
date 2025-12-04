package upc;

import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;
import upc.model.jeu.TerrainGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAlien {

    @Test
    public void testDeplacementDansLimites() {
        Team team = new Team("Team A", 1);
        Terrain terrain = TerrainGenerator.createTerrain("");
        Alien alien = new Alien(team, new Position(2, 2));

        assertTrue(alien.moveRight(terrain));
        assertEquals(7, alien.getPosition().getX());
        assertEquals(2, alien.getPosition().getY());
    }

    @Test
    public void testDeplacementHorsLimite() {
        Team team = new Team("Team B", 1);
        Terrain terrain = TerrainGenerator.createTerrain("");

        Alien alien = new Alien(team, new Position(0, 0));
        assertFalse(alien.moveLeft(terrain));

        assertEquals(0, alien.getPosition().getX());
        assertEquals(0, alien.getPosition().getY());
    }

    @Test
    public void testImpossibiliteDeBougerSiMort() {
        Team team = new Team("Team C", 1);
        Terrain terrain = TerrainGenerator.createTerrain("");

        Alien alien = new Alien(team, new Position(2, 2));
        alien.receiveDmg(100);

        assertFalse(alien.moveRight(terrain));
        assertEquals(2, alien.getPosition().getX());
        assertEquals(2, alien.getPosition().getY());
        assertFalse(alien.isAlive());
    }

    @Test
    public void testPriseDeDegats() {
        Team team = new Team("Team D", 1);
        Alien alien = new Alien(team, new Position(2, 2));

        alien.receiveDmg(90);
        assertEquals(10, alien.getHealthPoints());
        assertTrue(alien.isAlive());

        alien.receiveDmg(10);
        assertEquals(0, alien.getHealthPoints());
        assertFalse(alien.isAlive());
    }

    @Test
    public void testReceiveDmg() {
        Team team = new Team("Team A", 1);
        Alien alien = new Alien(team, new Position(50, 50));

        alien.receiveDmg(30);
        assertEquals(70, alien.getHealthPoints());
        assertTrue(alien.isAlive());
    }

    @Test
    public void testDeplacementAvecPosition() {
        Team team = new Team("Team A", 1);
        Alien alien = new Alien(team, new Position(50, 25));
        Terrain terrain = TerrainGenerator.createEmptyTerrain(100,100);

        assertTrue(alien.moveRight(terrain));
        assertEquals(55, alien.getPosition().getX());
        assertEquals(25, alien.getPosition().getY());
    }

    @Test
    public void testPositionImmutable() {
        Position p1 = new Position(10, 20);
        Position p2 = p1.deplacer(5, 0);

        assertEquals(10, p1.getX());
        assertEquals(15, p2.getX());
    }

    @Test
    public void testMoveLeft() {
        Team team = new Team("Team E", 1);
        Terrain terrain = TerrainGenerator.createEmptyTerrain(100, 100);
        Alien alien = new Alien(team, new Position(10, 25));

        assertTrue(alien.moveLeft(terrain));
        assertEquals(5, alien.getPosition().getX());
        assertEquals(25, alien.getPosition().getY());
    }

    @Test
    public void testJump() {
        Team team = new Team("Team F", 1);
        Terrain terrain = TerrainGenerator.createEmptyTerrain(100, 100);
        Alien alien = new Alien(team, new Position(50, 50));

        assertTrue(alien.jump(terrain));
        assertEquals(50, alien.getPosition().getX());
        assertEquals(30, alien.getPosition().getY());
    }

    @Test
    public void testDeplacementVerMortNeBougePas() {
        Team team = new Team("Team G", 1);
        Terrain terrain = TerrainGenerator.createEmptyTerrain(100, 100);
        Alien alien = new Alien(team, new Position(5, 5));

        alien.receiveDmg(100);
        assertFalse(alien.moveRight(terrain));
    }

    @Test
    public void testDegatsNegatifsSontIgnores() {
        Team team = new Team("Team H", 1);
        Terrain terrain = TerrainGenerator.createEmptyTerrain(100, 100);

        Alien alien = new Alien(team, new Position(5, 5));
        alien.receiveDmg(-50);
        assertEquals(100, alien.getHealthPoints());
    }

    @Test
    public void testEquipeTousElimines() {
        Team team = new Team("Team A", 2);
        team.initializeTeam("", 0);

        Alien[] aliens = team.getAliens();
        assertFalse(team.isEliminated());

        aliens[0].receiveDmg(100);
        aliens[1].receiveDmg(100);

        assertTrue(team.isEliminated());
    }

    @Test
    public void testGetTeam() {
        Team team = new Team("Team Test", 1);
        Alien alien = new Alien(team, new Position(50, 50));

        assertEquals(team, alien.getTeam());
        assertEquals("Team Test", alien.getTeam().getName());
    }

    @Test
    public void testGetSpeed() {
        Team team = new Team("Team Test", 1);
        Alien alien = new Alien(team, new Position(50, 50));

        assertEquals(5, alien.getSpeed());
    }

    @Test
    public void testToString() {
        Team team = new Team("Team Test", 1);
        Alien alien = new Alien(team, new Position(50, 50));

        String result = alien.toString();
        assertTrue(result.contains("PV=100"));
        assertTrue(result.contains("Vivant=true"));
    }

    @Test
    public void testToStringWhenDead() {
        Team team = new Team("Team Test", 1);
        Alien alien = new Alien(team, new Position(50, 50));
        alien.receiveDmg(100);

        String result = alien.toString();
        assertTrue(result.contains("PV=0"));
        assertTrue(result.contains("Vivant=false"));
    }

    @Test
    public void testIsVisibleDansLaCamera() {
        Team team = new Team("Team Test", 1);
        Alien alien = new Alien(team, new Position(500, 300));

        assertTrue(alien.isVisible(400, 200));
    }
    @Test
    public void testIsVisibleHorsCameraGauche() {
        Team team = new Team("Team Test", 1);
        Alien alien = new Alien(team, new Position(50, 50));

        assertFalse(alien.isVisible(1000, 500));
    }

    @Test
    public void testIsVisibleQuandMort() {
        Team team = new Team("Team Test", 1);
        Alien alien = new Alien(team, new Position(500, 300));
        alien.receiveDmg(100);

        assertFalse(alien.isVisible(400, 200));
    }
    @Test
    public void testReceiveDmgSuperieurAuxPV() {
        Team team = new Team("Team Test", 1);
        Alien alien = new Alien(team, new Position(50, 50));

        alien.receiveDmg(150);

        assertEquals(0, alien.getHealthPoints());
        assertFalse(alien.isAlive());
    }

    @Test
    public void testReceiveDmgMultiples() {
        Team team = new Team("Team Test", 1);
        Alien alien = new Alien(team, new Position(50, 50));

        alien.receiveDmg(20);
        alien.receiveDmg(30);
        alien.receiveDmg(40);

        assertEquals(10, alien.getHealthPoints());
        assertTrue(alien.isAlive());
    }







}
