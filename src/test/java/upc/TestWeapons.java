package upc;

import org.junit.jupiter.api.Test;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.entites.Projectile;
import upc.model.items.Grenade;
import upc.model.items.Uzi;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Team;

import static org.junit.jupiter.api.Assertions.*;

public class TestWeapons {

    @Test
    public void testGrenadeConstantes() {
        assertEquals(45, Grenade.DEGATS);
        assertEquals(5, Grenade.RAYON);
    }

    @Test
    public void testGrenadeConstructeur() {
        Grenade grenade = new Grenade();

        assertEquals("Grenade", grenade.getName());
        assertEquals(45, grenade.getDamage());
    }

    @Test
    public void testUziConstantes() {
        assertEquals(2, Uzi.DEGATS);
        assertEquals(25, Uzi.NBTIR);
    }

    @Test
    public void testUziConstructeur() {
        Uzi uzi = new Uzi();

        assertEquals("Uzi", uzi.getName());
        assertEquals(2, uzi.getDamage());
    }
}
