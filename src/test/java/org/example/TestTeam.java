package org.example;
import org.example.model.entites.Alien;
import org.example.model.jeu.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestTeam {
    //testConstructeurEtTaille
    @Test
    public void testConstructeur() {
        Team a = new Team("TEAM A",3);
        assertEquals("TEAM A",a.getNom());
        assertEquals(3,a.getWorms().length);
    }
    //Test InitialiserEquipeCreeLesWorms
    @Test
    public void testInitialiserEquipe() {
        Team a = new Team("TEAM A",3);
        a.initialiserEquipe("",0);
        Alien[] aliens = a.getWorms() ;
        for(Alien w : aliens){
            assertNotNull(w);
        }
    }
    @Test
    public void testEquipeEliminee() {
        Team b = new Team("TEAM B",3);
        b.initialiserEquipe("",1);
        Alien[] aliens = b.getWorms() ;
        aliens[0].receiveDmg(100);
        boolean estEliminee = b.estEliminee();
        assertFalse(estEliminee);
        assertFalse(aliens[0].isAlive());
        assertTrue(aliens[1].isAlive());
    }
    @Test
    public void testGetNom() {
        Team a = new Team("TEAM A",3);
        assertEquals("TEAM A",a.getNom());
    }
    @Test
    public void testGetWorms() {
        Team a = new Team("TEAM A",3);
        assertEquals(a.getWorms(),a.getWorms());
    }



}

