package org.example;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Terrain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestWorm {

    @Test
    public void testDeplacementDansLimites() {
        Equipe equipe = new Equipe("Team A", 1);
        Terrain terrain = Terrain.creerTerrainVide(100, 100);
        Worm worm = new Worm(equipe, 2, 2);

        assertTrue(worm.deplacerDroite(terrain));
        assertEquals(7, worm.getPosition().getX());
        assertEquals(2, worm.getPosition().getY());
    }

    @Test
    public void testDeplacementHorsLimite() {
        Equipe equipe = new Equipe("Team B", 1);
        Terrain terrain = Terrain.creerTerrainVide(10, 10);

        Worm worm = new Worm(equipe, 0, 0);
        assertFalse(worm.deplacerGauche(terrain));

        assertEquals(0, worm.getPosition().getX());
        assertEquals(0, worm.getPosition().getY());
    }

    @Test
    public void testImpossibiliteDeBougerSiMort() {
        Equipe equipe = new Equipe("Team C", 1);
        Terrain terrain = Terrain.creerTerrainVide(100, 100);

        Worm worm = new Worm(equipe, 2, 2);
        worm.subirDegats(100);

        assertFalse(worm.deplacerDroite(terrain));
        assertEquals(2, worm.getPosition().getX());
        assertEquals(2, worm.getPosition().getY());
        assertFalse(worm.estVivant());
    }

    @Test
    public void testPriseDeDegats() {
        Equipe equipe = new Equipe("Team D", 1);
        Worm worm = new Worm(equipe, 2, 2);

        worm.subirDegats(90);
        assertEquals(10, worm.getPointsDeVie());
        assertTrue(worm.estVivant());

        worm.subirDegats(10);
        assertEquals(0, worm.getPointsDeVie());
        assertFalse(worm.estVivant());
    }

    @Test
    public void testSubirDegats() {
        Equipe equipe = new Equipe("Team A", 1);
        Worm worm = new Worm(equipe, 50, 50);

        worm.subirDegats(30);
        assertEquals(70, worm.getPointsDeVie());
        assertTrue(worm.estVivant());
    }

    @Test
    public void testDeplacementAvecPosition() {
        Equipe equipe = new Equipe("Team A", 1);
        Worm worm = new Worm(equipe, 50, 50);
        Terrain terrain = Terrain.creerTerrainVide(200, 200);

        assertTrue(worm.deplacerDroite(terrain));
        assertEquals(55, worm.getPosition().getX());
        assertEquals(50, worm.getPosition().getY());
    }

    @Test
    public void testPositionImmutable() {
        Position p1 = new Position(10, 20);
        Position p2 = p1.deplacer(5, 0);

        assertEquals(10, p1.getX());
        assertEquals(15, p2.getX());
    }

    @Test
    public void testDeplacerGauche() {
        Equipe equipe = new Equipe("Team E", 1);
        Terrain terrain = Terrain.creerTerrainVide(100, 100);
        Worm worm = new Worm(equipe, 10, 50);

        assertTrue(worm.deplacerGauche(terrain));
        assertEquals(5, worm.getPosition().getX());
        assertEquals(50, worm.getPosition().getY());
    }

    @Test
    public void testSauter() {
        Equipe equipe = new Equipe("Team F", 1);
        Terrain terrain = Terrain.creerTerrainVide(100, 100);
        Worm worm = new Worm(equipe, 50, 50);

        assertTrue(worm.sauter(terrain));
        assertEquals(50, worm.getPosition().getX());
        assertEquals(30, worm.getPosition().getY());
    }

    @Test
    public void testDeplacementVerMortNeBougePas() {
        Equipe equipe = new Equipe("Team G", 1);
        Terrain terrain = Terrain.creerTerrainVide(100, 100);
        Worm worm = new Worm(equipe, 5, 5);

        worm.subirDegats(100);
        assertFalse(worm.deplacerDroite(terrain));
    }

    @Test
    public void testDegatsNegatifsSontIgnores() {
        Equipe equipe = new Equipe("Team H", 1);
        Terrain terrain = Terrain.creerTerrainVide(100, 100);

        Worm worm = new Worm(equipe, 5, 5);
        worm.subirDegats(-50);
        assertEquals(100, worm.getPointsDeVie());
    }

    @Test
    public void testEquipeTousElimines() {
        Equipe equipe = new Equipe("Team A", 2);
        equipe.initialiserEquipe(10, 10, 50, 20);

        Worm[] worms = equipe.getWorms();
        assertFalse(equipe.estEliminee());

        worms[0].subirDegats(100);
        worms[1].subirDegats(100);

        assertTrue(equipe.estEliminee());
    }
}
