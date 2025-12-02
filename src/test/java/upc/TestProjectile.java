package upc;

import upc.model.jeu.Team;
import upc.model.entites.Position;
import upc.model.items.Weapon;
import upc.model.entites.Alien;
import upc.model.jeu.GameWorld;
import upc.model.entites.Projectile;
import org.junit.jupiter.api.Test;
import upc.model.entites.Projectile.ProjectileState;
import static org.junit.jupiter.api.Assertions.*;


public class TestProjectile {

    // Classe interne nécessaire car Weapon est abstraite.
    // Elle permet de créer une vraie instance d'arme pour les tests.
    static class TestWeapon extends Weapon {
        public TestWeapon() {
            super("TestBazooka", 50, 100);
        }
        @Override
        public double getInitialVelocity() { return 10.0; }
        @Override
        public boolean isAffectedByWind() { return true; }
        @Override
        public void utiliser(GameWorld gameWorld, Alien alienActif) { }
    }

    @Test
    void testConstructor() {
        Team team = new Team("Alpha", 1);
        Position pos = new Position(10, 20);
        Alien shooter = new Alien(team, pos);
        Weapon weapon = new TestWeapon();

        Projectile p = new Projectile(weapon, shooter, 10, 20);

        assertNotNull(p);
        assertEquals(10, p.getPosition().getX());
        assertEquals(20, p.getPosition().getY());
        assertEquals(1, p.getSpeed());
        assertEquals(ProjectileState.FLYING, p.getState());
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

    @Test
    void testGetFrameCount() {
        Projectile p = new Projectile(null, null, 0, 0);
        assertEquals(0, p.getFrameCount());
    }

}
