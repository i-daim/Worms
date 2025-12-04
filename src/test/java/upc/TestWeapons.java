package upc;

import org.junit.jupiter.api.Test;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.entites.Projectile;
import upc.model.items.Grenade;
import upc.model.items.Uzi;
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
        Grenade grenade = new Grenade("Ma grenade");

        assertEquals("Ma grenade", grenade.getNom());
        assertEquals(45, grenade.getDegats());
        assertEquals(5, grenade.getRayonExplosion());
    }

    @Test
    public void testUziConstantes() {
        assertEquals(2, Uzi.DEGATS);
        assertEquals(10, Uzi.RAYON);
        assertEquals(25, Uzi.NBTIR);
    }

    @Test
    public void testUziConstructeur() {
        Uzi uzi = new Uzi("Mon Uzi");

        assertEquals("Mon Uzi", uzi.getNom());
        assertEquals(2, uzi.getDegats());
        assertEquals(10, uzi.getRayonExplosion());
    }

    @Test
    public void testCreateProjectile() {
        Team team = new Team("Test Team", 1);
        Alien alien = new Alien(team, new Position(100, 200));
        Grenade grenade = new Grenade("Grenade");

        Projectile projectile = grenade.createProjectile(alien);

        assertNotNull(projectile);
        assertEquals(100, projectile.getPosition().getX());
        assertEquals(200, projectile.getPosition().getY());
        assertEquals(grenade, projectile.getWeapon());
        assertEquals(alien, projectile.getShooter());
    }



}
