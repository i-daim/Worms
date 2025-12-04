package upc;

import org.junit.jupiter.api.Test;
import upc.model.items.Grenade;
import upc.model.items.Uzi;

import static org.junit.jupiter.api.Assertions.*;

public class TestWeapons {

    @Test
    public void testGrenadeConstantes() {
        assertEquals(45, Grenade.DEGATS);
        assertEquals(5, Grenade.RAYON);
    }

    @Test
    public void testGrenadeConstructeur() {
        Grenade grenade = new Grenade("Ma grenade");

        assertEquals("Ma grenade", grenade.getNom());
        assertEquals(45, grenade.getDegats());
        assertEquals(5, grenade.getRayonExplosion());
    }

}
