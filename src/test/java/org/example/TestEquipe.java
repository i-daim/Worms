package org.example;
import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestEquipe {
    //testConstructeurEtTaille
    @Test
    public void testConstructeur() {
        Equipe a = new Equipe("TEAM A",3);
        assertEquals("TEAM A",a.getNom());
        assertEquals(3,a.getWorms().length);
    }
    //Test InitialiserEquipeCreeLesWorms
    @Test
    public void testInitialiserEquipe() {
        Equipe a = new Equipe("TEAM A",3);
        a.initialiserEquipe("",0);
        Worm[] worms = a.getWorms() ;
        for(Worm w : worms){
            assertNotNull(w);
        }
    }
    @Test
    public void testEquipeEliminee() {
        Equipe b = new Equipe("TEAM B",3);
        b.initialiserEquipe("",1);
        Worm[] worms = b.getWorms() ;
        worms[0].receiveDmg(100);
        boolean estEliminee = b.estEliminee();
        assertFalse(estEliminee);
        assertFalse(worms[0].isAlive());
        assertTrue(worms[1].isAlive());
    }
    @Test
    public void testGetNom() {
        Equipe a = new Equipe("TEAM A",3);
        assertEquals("TEAM A",a.getNom());
    }
    @Test
    public void testGetWorms() {
        Equipe a = new Equipe("TEAM A",3);
        assertEquals(a.getWorms(),a.getWorms());
    }



}

