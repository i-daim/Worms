package upc;

import upc.model.entites.Projectile;
import org.junit.jupiter.api.Test;
import upc.model.entites.Projectile.ProjectileState;
import static org.junit.jupiter.api.Assertions.*;


public class TestProjectile {

    @Test
    void testConstructor() {
        Projectile p = new Projectile(null, null, 10, 20);
        assertNotNull(p);
        assertEquals(ProjectileState.FLYING, p.getState());
        assertEquals(1, p.getSpeed());
    }

    @Test
    void testGetWeapon() {
        Projectile p = new Projectile(null, null, 0, 0);
        assertNull(p.getWeapon());
    }

    @Test
    void testGetShooter() {
        Projectile p = new Projectile(null, null, 0, 0);
        assertNull(p.getShooter());
    }

    @Test
    void testGetBody() {
        Projectile p = new Projectile(null, null, 0, 0);
        assertNull(p.getBody());
    }

    @Test
    void testSetBody() {
        Projectile p = new Projectile(null, null, 0, 0);
        p.setBody(null);
        assertNull(p.getBody());
    }

    @Test
    void testGetState() {
        Projectile p = new Projectile(null, null, 0, 0);
        assertEquals(ProjectileState.FLYING, p.getState());
    }

}
