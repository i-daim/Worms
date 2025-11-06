package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.model.entites.Worm;
import org.example.model.entites.Position;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Monde;

public class TestWorm {

    @Test
    public void testDeplacementDansLimites() {
        Equipe equipe = new Equipe("Team A", 1);
        Monde monde = new Monde(10, 10);
        Worm worm = new Worm(100, equipe, 2, 2);

        worm.deplacerDroite(monde);
        assertEquals(7, worm.getPosition().x);
        assertEquals(2, worm.getPosition().y);
    }

    @Test
    public void testDeplacementHorsLimite() {
        Equipe equipe = new Equipe("Team B", 1);
        Monde monde = new Monde(10, 10);

        Worm worm = new Worm(100, equipe, 0, 0);
        worm.deplacerGauche(monde);

        assertEquals(0, worm.getPosition().x);
        assertEquals(0, worm.getPosition().y);
    }

    @Test
    public void testImpossibiliteDeBougerSiMort() {
        Equipe equipe = new Equipe("Team C", 1);
        Monde monde = new Monde(10, 10);

        Worm worm = new Worm(0, equipe, 2, 2);
        worm.deplacerDroite(monde);

        assertEquals(2, worm.getPosition().x);
        assertEquals(2, worm.getPosition().y);
        assertFalse(worm.estVivant());
    }

    @Test
    public void testPriseDeDegats() {
        Equipe equipe = new Equipe("Team D", 1);
        Worm worm = new Worm(10, equipe, 2, 2);

        worm.subirDegats(5);
        assertEquals(5, worm.getPointsDeVie());
        assertTrue(worm.estVivant());

        worm.subirDegats(10);
        assertEquals(0, worm.getPointsDeVie());
        assertFalse(worm.estVivant());
    }

    @Test
    public void testSubirDegats() {
        Equipe equipe = new Equipe("Team A", 1);
        Worm worm = new Worm(100, equipe, 50, 50);

        worm.subirDegats(30);
        assertEquals(70, worm.getPointsDeVie());
        assertTrue(worm.estVivant());
    }

    @Test
    public void testDeplacementAvecPosition() {
        Equipe equipe = new Equipe("Team A", 1);
        Worm worm = new Worm(100, equipe, 50, 50);
        Monde monde = new Monde(200, 200);

        assertTrue(worm.deplacerDroite(monde));
        assertEquals(55, worm.getPosition().x);
        assertEquals(50, worm.getPosition().y);
    }

    @Test
    public void testPositionImmutable() {
        Position p1 = new Position(10, 20);
        Position p2 = p1.deplacer(5, 0);

        assertEquals(10, p1.x);
        assertEquals(15, p2.x);
    }

    @Test
    public void testDeplacerGauche() {
        Equipe equipe = new Equipe("Team E", 1);
        Monde monde = new Monde(100, 100);
        Worm worm = new Worm(100, equipe, 10, 50);

        assertTrue(worm.deplacerGauche(monde));
        assertEquals(5, worm.getPosition().x);
        assertEquals(50, worm.getPosition().y);
    }

    @Test
    public void testSauter() {
        Equipe equipe = new Equipe("Team F", 1);
        Monde monde = new Monde(100, 100);
        Worm worm = new Worm(100, equipe, 50, 50);

        assertTrue(worm.sauter(monde));
        assertEquals(50, worm.getPosition().x);
        assertEquals(30, worm.getPosition().y);
    }
    @Test
    public void testDeplacementVerMortNeBougePas() {
        Equipe equipe = new Equipe("Team G", 1);
        Monde monde = new Monde(100, 100);
        Worm worm = new Worm(0, equipe, 5, 5);
        assertFalse(worm.deplacerDroite(monde));
    }

    @Test
    public void testDegatsNegatifsSontIgnores() {
        Equipe equipe = new Equipe("Team H", 1);
        Monde monde = new Monde(100, 100);

        Worm worm = new Worm(100, equipe, 5, 5);
        worm.subirDegats(-50);
        assertEquals(100, worm.getPointsDeVie());
    }
   /* @Test
    public void testEquipeTousElimines() {
        Equipe equipe = new Equipe("Team A", 2);
        Worm worm1 = new Worm(10, equipe, 10, 50);
        Worm worm2 = new Worm(10, equipe, 20, 50);

        equipe.ajouterWorm(0, worm1);
        equipe.ajouterWorm(1, worm2);

        assertFalse(equipe.tousElimines());

        worm1.subirDegats(100);
        worm2.subirDegats(100);

        assertTrue(equipe.tousElimines());
    }
       */
}
