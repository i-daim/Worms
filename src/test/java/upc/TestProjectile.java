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



}
