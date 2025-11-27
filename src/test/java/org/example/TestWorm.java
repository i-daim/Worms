package org.example;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Terrain;
import org.example.model.jeu.TerrainGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestWorm {

    @Test
    public void testDeplacementDansLimites() {
        Equipe equipe = new Equipe("Team A", 1);
        Terrain terrain = TerrainGenerator.creerTerrain("");
        Worm worm = new Worm(equipe, new Position(2, 2));

        assertTrue(worm.moveRight(terrain));
        assertEquals(7, worm.getPosition().getX());
        assertEquals(2, worm.getPosition().getY());
    }

    @Test
    public void testDeplacementHorsLimite() {
        Equipe equipe = new Equipe("Team B", 1);
        Terrain terrain = TerrainGenerator.creerTerrain("");

        Worm worm = new Worm(equipe, new Position(0, 0));
        assertFalse(worm.moveLeft(terrain));

        assertEquals(0, worm.getPosition().getX());
        assertEquals(0, worm.getPosition().getY());
    }

    @Test
    public void testImpossibiliteDeBougerSiMort() {
        Equipe equipe = new Equipe("Team C", 1);
        Terrain terrain = TerrainGenerator.creerTerrain("");

        Worm worm = new Worm(equipe, new Position(2, 2));
        worm.receiveDmg(100);

        assertFalse(worm.moveRight(terrain));
        assertEquals(2, worm.getPosition().getX());
        assertEquals(2, worm.getPosition().getY());
        assertFalse(worm.isAlive());
    }

    @Test
    public void testPriseDeDegats() {
        Equipe equipe = new Equipe("Team D", 1);
        Worm worm = new Worm(equipe, new Position(2, 2));

        worm.receiveDmg(90);
        assertEquals(10, worm.getHealthPoints());
        assertTrue(worm.isAlive());

        worm.receiveDmg(10);
        assertEquals(0, worm.getHealthPoints());
        assertFalse(worm.isAlive());
    }

    @Test
    public void testReceiveDmg() {
        Equipe equipe = new Equipe("Team A", 1);
        Worm worm = new Worm(equipe, new Position(50, 50));

        worm.receiveDmg(30);
        assertEquals(70, worm.getHealthPoints());
        assertTrue(worm.isAlive());
    }

    @Test
    public void testDeplacementAvecPosition() {
        Equipe equipe = new Equipe("Team A", 1);
        Worm worm = new Worm(equipe, new Position(50, 25));
        Terrain terrain = TerrainGenerator.creerTerrainVide(100,100);

        assertTrue(worm.moveRight(terrain));
        assertEquals(55, worm.getPosition().getX());
        assertEquals(25, worm.getPosition().getY());
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
        Equipe equipe = new Equipe("Team E", 1);
        Terrain terrain = TerrainGenerator.creerTerrainVide(100, 100);
        Worm worm = new Worm(equipe, new Position(10, 25));

        assertTrue(worm.moveLeft(terrain));
        assertEquals(5, worm.getPosition().getX());
        assertEquals(25, worm.getPosition().getY());
    }

    @Test
    public void testJump() {
        Equipe equipe = new Equipe("Team F", 1);
        Terrain terrain = TerrainGenerator.creerTerrainVide(100, 100);
        Worm worm = new Worm(equipe, new Position(50, 50));

        assertTrue(worm.jump(terrain));
        assertEquals(50, worm.getPosition().getX());
        assertEquals(30, worm.getPosition().getY());
    }

    @Test
    public void testDeplacementVerMortNeBougePas() {
        Equipe equipe = new Equipe("Team G", 1);
        Terrain terrain = TerrainGenerator.creerTerrainVide(100, 100);
        Worm worm = new Worm(equipe, new Position(5, 5));

        worm.receiveDmg(100);
        assertFalse(worm.moveRight(terrain));
    }

    @Test
    public void testDegatsNegatifsSontIgnores() {
        Equipe equipe = new Equipe("Team H", 1);
        Terrain terrain = TerrainGenerator.creerTerrainVide(100, 100);

        Worm worm = new Worm(equipe, new Position(5, 5));
        worm.receiveDmg(-50);
        assertEquals(100, worm.getHealthPoints());
    }

    @Test
    public void testEquipeTousElimines() {
        Equipe equipe = new Equipe("Team A", 2);
        equipe.initialiserEquipe("", 0);

        Worm[] worms = equipe.getWorms();
        assertFalse(equipe.estEliminee());

        worms[0].receiveDmg(100);
        worms[1].receiveDmg(100);

        assertTrue(equipe.estEliminee());
    }
}
