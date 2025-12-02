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

    @Test
    void testSetState() {
        Projectile p = new Projectile(null, null, 0, 0);

        p.setState(ProjectileState.EXPLODED);
        assertEquals(ProjectileState.EXPLODED, p.getState());

        p.setState(ProjectileState.OUT_OF_BOUNDS);
        assertEquals(ProjectileState.OUT_OF_BOUNDS, p.getState());
    }

    @Test
    void testIsActive() {
        Projectile p = new Projectile(null, null, 0, 0);

        p.setState(ProjectileState.FLYING);
        assertTrue(p.isActive());

        p.setState(ProjectileState.EXPLODED);
        assertFalse(p.isActive());
    }

    @Test
    void testIncrementFrame() {
        Projectile p = new Projectile(null, null, 0, 0);
        int initial = p.getFrameCount();
        p.incrementFrame();
        assertEquals(initial + 1, p.getFrameCount());
    }

}
