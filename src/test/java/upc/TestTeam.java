package upc;
import org.junit.jupiter.api.BeforeEach;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.GameManager;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Classe de test unitaire pour la classe Team.
 * Vérifie le cycle de vie d'une équipe, la gestion des Aliens et les conditions d'élimination.
 */
public class TestTeam {

    private GameWorld world;
    /**
     * Prépare l'environnement de test avant chaque méthode.
     * Initialise un terrain vide et un monde de jeu.
     */
    @BeforeEach
    public void setUp() {
        upc.model.jeu.Terrain terrainTest = upc.model.jeu.TerrainGenerator.createEmptyTerrain(100, 50);
        GameManager manager = new GameManager(terrainTest, 2);

        world = manager.getWorld();
    }
    /**
     * Vérifie que le constructeur initialise correctement le nom et la taille de l'équipe.
     */
    @Test
    public void testConstructeur() {
        Team a = new Team("TEAM A", 3);
        assertEquals("TEAM A", a.getName());
        assertEquals(3, a.getAliens().length);
    }
    /**
     * Vérifie que l'initialisation de l'équipe crée bien des instances d'Aliens non nulles.
     */
    @Test
    public void testInitializeTeam() {
        Team a = new Team("TEAM A", 3);
        a.initializeTeam(world, "", 0);
        Alien[] aliens = a.getAliens();
        for (Alien w : aliens) {
            assertNotNull(w);
        }
    }
    /**
     * Vérifie qu'une équipe est considérée comme éliminée seulement
     * lorsque tous ses membres ont perdu tous leurs points de vie.
     */
    @Test
    public void testEquipeEliminee() {
        Team b = new Team("TEAM B", 3);
        b.initializeTeam(world, "", 1);

        Alien[] aliens = b.getAliens();
        aliens[0].receiveDmg(100);

        assertFalse(b.isEliminated());
        assertFalse(aliens[0].isAlive());
        assertTrue(aliens[1].isAlive());

        aliens[1].receiveDmg(100);
        aliens[2].receiveDmg(100);

        assertTrue(b.isEliminated());
    }
    /**
     * Vérifie que la méthode getName() retourne le bon nom d'équipe.
     */
    @Test
    public void testGetNom() {
        Team a = new Team("TEAM A", 3);
        assertEquals("TEAM A", a.getName());
    }
    /**
     * Vérifie l'accès à la liste des Aliens de l'équipe.
     */
    @Test
    public void testGetAliens() {
        Team a = new Team("TEAM A", 3);
        assertEquals(a.getAliens(), a.getAliens());
    }
    /**
     * Vérifie la récupération de l'Alien actuellement actif dans le tour de jeu.
     */
    @Test
    public void testGetActiveAlien() {
        Team a = new Team("TEAM A", 3);
        Alien activeAlien = a.getActiveAlien();
        assertNull(activeAlien);

        a.getAliens()[0] = new Alien(world, a, new Position(0, 1));
        assertNotNull(a.getActiveAlien());
    }
    /**
     * Vérifie que le compteur d'Aliens vivants se met à jour correctement.
     */
    @Test
    public void testGetAliveAliensNumber() {
        Team a = new Team("TEAM A", 3);
        assertEquals(0, a.getAliveAliensNumber());

        a.getAliens()[0] = new Alien(world, a, new Position(0, 1));
        assertEquals(1, a.getAliveAliensNumber());
    }
    /**
     * Vérifie si l'Alien actuel est bien le dernier de la liste des membres.
     */

    @Test
    public void testIsActiveLastAlien() {
        Team a = new Team("TEAM A", 3);
        assertTrue(a.isActiveLastAlien());

        a.getAliens()[0] = new Alien(world, a, new Position(0, 1));
        a.getAliens()[1] = new Alien(world, a, new Position(1, 0));
        assertFalse(a.isActiveLastAlien());
    }
    /**
     * Vérifie que l'index de l'Alien actif peut être réinitialisé .
     */
    @Test
    public void testResetIndex() {
        Team a = new Team("TEAM A", 2);
        int activeAlien = a.getActiveAlienIndex();
        a.resetIndex();
        assertNotEquals(activeAlien, a.getActiveAlienIndex());

        a.getAliens()[0] = new Alien(world, a, new Position(0, 1));
        a.getAliens()[1] = new Alien(world, a, new Position(1, 0));
        a.resetIndex();
        assertEquals(activeAlien, a.getActiveAlienIndex());

    }

    @Test
    public void testSkipToNextAlien() {
        Team a = new Team("TEAM A", 2);
        a.getAliens()[0] = new Alien(world, a, new Position(0, 1));
        a.getAliens()[1] = new Alien(world, a, new Position(1, 0));
        Alien activeAlien = a.getActiveAlien();
        a.skipToNextAlien();
        assertNotEquals(activeAlien,a.getActiveAlien());
    }

    @Test
    public void testToString() {
        Team a = new Team("TEAM A", 2);
        String actual = a.getName() + " (" + a.getAliveAliensNumber() + "/" + a.getAliens().length + " vivants)";
        assertEquals(actual, a.toString());
    }
}

