package upc;

import org.junit.jupiter.api.Test;
import upc.model.items.Grenade;
import upc.model.items.Uzi;
import upc.model.items.Shotgun;


import static org.junit.jupiter.api.Assertions.*;

public class TestWeapons {

    @Test
    public void testUziConstructeur() {
        Uzi uzi = new Uzi();

        assertEquals("Uzi", uzi.getName());
        assertEquals(10, uzi.getDamage());
        assertEquals(30, uzi.getNbOfShots());
        assertEquals(80, uzi.getPuissanceTir());
    }
    @Test
    public void testGrenadeConstructeur() {
        Grenade grenade = new Grenade();

        assertEquals("Grenade", grenade.getName());
        assertEquals(50, grenade.getDamage());
        assertEquals(3, grenade.getNbOfShots());
        assertEquals(50, grenade.getPuissanceTir());
    }
    @Test
    public void testShotgunConstructeur() {
        Shotgun shotgun = new Shotgun();

        assertEquals("Shotgun", shotgun.getName());
        assertEquals(35, shotgun.getDamage());
        assertEquals(8, shotgun.getNbOfShots());
        assertEquals(60, shotgun.getPuissanceTir());
    }
}
