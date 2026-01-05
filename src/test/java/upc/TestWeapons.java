package upc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upc.model.items.Grenade;
import upc.model.items.Uzi;
import upc.model.items.Shotgun;
import upc.model.items.Mine;
import upc.model.items.Bazooka;
import upc.model.entites.Position;
import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Team;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Classe de tests unitaires pour les armes du jeu.
 * Vérifie l'initialisation et le comportement des armes.
 */
public class TestWeapons {

    private Alien dummyShooter;
    /**
     * création d'un environnement (terrain, monde, équipe)
     * afin d'avoir un Alien valide pour les tests.
     */
    @BeforeEach
    public void setUp() {
        // Crée un environnement minimal pour construire un Alien valide
        upc.model.jeu.Terrain terrainTest = upc.model.jeu.TerrainGenerator.createEmptyTerrain(100, 50);
        GameManager manager = new GameManager(terrainTest, 2);
        GameWorld world = manager.getWorld();
        Team team = new Team("TestTeam", 1);

        // Position arbitraire (0,0)
        dummyShooter = new Alien(world, team, new Position(0, 0));
        dummyShooter.setAimAngle(0);
        dummyShooter.setShootPower(0);
    }
    /**
     * Vérifie que le constructeur de l'Uzi initialise correctement
     * ses attributs (nom, dégâts, nombre de tirs et puissance).
     */
    @Test
    public void testUziConstructeur() {
        Uzi uzi = new Uzi();

        assertEquals("Uzi", uzi.getName());
        assertEquals(10, uzi.getDamage());
        assertEquals(30, uzi.getNbOfShots());
        assertEquals(20, uzi.getBasePower());
    }
    @Test
    public void testGrenadeConstructeur() {
        Grenade grenade = new Grenade();

        assertEquals("Grenade", grenade.getName());
        assertEquals(50, grenade.getDamage());
        assertEquals(1, grenade.getNbOfShots());
        assertEquals(10, grenade.getBasePower());
    }
    @Test
    public void testShotgunConstructeur() {
        Shotgun shotgun = new Shotgun();

        assertEquals("Shotgun", shotgun.getName());
        assertEquals(35, shotgun.getDamage());
        assertEquals(8, shotgun.getNbOfShots());
        assertEquals(20, shotgun.getBasePower());
    }
    @Test
    public void testMineConstantes() {
        assertEquals(45, Mine.DAMAGE);
        assertEquals(30, Mine.EXPLOSION_RADIUS);
        assertEquals(20, Mine.ACTIVATION_RADIUS);
    }

    @Test
    public void testMineConstructeur() {
        Mine mine = new Mine();

        assertEquals("Mine", mine.getName());
        assertEquals(45, mine.getDamage());
        assertEquals(1, mine.getNbOfShots());
        assertEquals(5, mine.getBasePower());
    }
    @Test
    public void testBazookaConstructeur() {
        Bazooka bazooka = new Bazooka();

        assertEquals("Bazooka", bazooka.getName());
        assertEquals(45, bazooka.getDamage());
        assertEquals(1, bazooka.getNbOfShots());
        assertEquals(20, bazooka.getBasePower());
    }
}
