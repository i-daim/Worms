package upc;

import org.junit.jupiter.api.Test;
import upc.model.items.Grenade;
import upc.model.items.Uzi;
import upc.model.items.Shotgun;
import upc.model.items.Mine;
import upc.model.items.Bazooka;
import upc.model.entites.projectiles.Rocket;
import upc.model.entites.Position;
import upc.model.entites.projectiles.Projectile;
import upc.model.entites.Alien;


import static org.junit.jupiter.api.Assertions.*;


public class TestWeapons {

    @Test
    public void testUziConstructeur() {
        Uzi uzi = new Uzi();

        assertEquals("Uzi", uzi.getName());
        assertEquals(10, uzi.getDamage());
        assertEquals(30, uzi.getNbOfShots());
        assertEquals(80, uzi.getSpeed());
    }
    @Test
    public void testGrenadeConstructeur() {
        Grenade grenade = new Grenade();

        assertEquals("Grenade", grenade.getName());
        assertEquals(50, grenade.getDamage());
        assertEquals(3, grenade.getNbOfShots());
        assertEquals(50, grenade.getSpeed());
    }
    @Test
    public void testShotgunConstructeur() {
        Shotgun shotgun = new Shotgun();

        assertEquals("Shotgun", shotgun.getName());
        assertEquals(35, shotgun.getDamage());
        assertEquals(8, shotgun.getNbOfShots());
        assertEquals(60, shotgun.getSpeed());
    }
    @Test
    public void testMineConstantes() {
        assertEquals(45, Mine.DAMAGE);
        assertEquals(3, Mine.EXPLOSION_RADIUS);
        assertEquals(3, Mine.ACTIVATION_RADIUS);
    }

    @Test
    public void testMineConstructeur() {
        Mine mine = new Mine();

        assertEquals("Mine", mine.getName());
        assertEquals(45, mine.getDamage());
        assertEquals(1, mine.getNbOfShots());
        assertEquals(15, mine.getSpeed());
    }
    @Test
    public void testBazookaConstructeur() {
        Bazooka bazooka = new Bazooka();

        assertEquals("Bazooka", bazooka.getName());
        assertEquals(45, bazooka.getDamage());
        assertEquals(3, bazooka.getNbOfShots());
        assertEquals(70, bazooka.getSpeed());
    }
    @Test
    public void testBazookaCreateProjectile() {
        Alien shooter = null;
        Bazooka bazooka = new Bazooka();
        Projectile p = bazooka.createProjectile(shooter, new Position(0,0), 45, 100);

        assertNotNull(p, "Le Bazooka doit créer un projectile");
        assertTrue(p instanceof Rocket, "Le Bazooka doit créer une Rocket");
    }

}
