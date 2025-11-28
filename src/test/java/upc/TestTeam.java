package upc;
import upc.model.entites.Alien;
import upc.model.jeu.Team;
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
        assertEquals("TEAM A",a.getName());
        assertEquals(3,a.getAliens().length);
    }
    //Test InitialiserEquipeCreeLesWorms
    @Test
    public void testInitializeTeam() {
        Team a = new Team("TEAM A",3);
        a.initializeTeam("",0);
        Alien[] aliens = a.getAliens() ;
        for(Alien w : aliens){
            assertNotNull(w);
        }
    }
    @Test
    public void testEquipeEliminee() {
        Team b = new Team("TEAM B",3);
        b.initializeTeam("",1);
        Alien[] aliens = b.getAliens() ;
        aliens[0].receiveDmg(100);
        boolean estEliminee = b.isEliminated();
        assertFalse(estEliminee);
        assertFalse(aliens[0].isAlive());
        assertTrue(aliens[1].isAlive());
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

