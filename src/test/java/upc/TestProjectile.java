package upc;

import upc.model.jeu.Team;
import upc.model.entites.Position;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Projectile;
import org.junit.jupiter.api.Test;
import upc.model.entites.projectiles.Projectile.State;
import static org.junit.jupiter.api.Assertions.*;


public class TestProjectile {

    @Test
    void testConstructor() {
        Team team = new Team("Alpha", 1);
        Position pos = new Position(10, 20);
        Alien shooter = new Alien(team, pos);

        Projectile p = new Projectile(shooter, pos, 20, 0, false, 1);

        assertNotNull(p);
        assertEquals(10, p.getPosition().getX());
        assertEquals(20, p.getPosition().getY());
        assertEquals(1, p.getSpeed());
        assertEquals(State.FLYING, p.getState());
    }

    @Test
    void testGetShooter() {
        Team team = new Team("Gamma", 1);
        Alien shooter = new Alien(team, new Position(50, 50));
        Projectile p = new Projectile(shooter, shooter.getPosition(), 0, 0, false, 0);

        assertEquals(shooter, p.getShooter());
        assertEquals("Gamma", p.getShooter().getTeam().getName());
    }

    @Test
    void testGetBody() {
        Team team = new Team("Delta", 1);
        Alien shooter = new Alien(team, new Position(0, 0));
        Projectile p = new Projectile(shooter, shooter.getPosition(), 0, 0, false, 0);

        assertNull(p.getBody());
    }

    @Test
    void testSetBody() {
        Team team = new Team("Epsilon", 1);
        Alien shooter = new Alien(team, new Position(0, 0));
        Projectile p = new Projectile(shooter, shooter.getPosition(), 0, 0, false, 0);

        p.setBody(null);

        assertNull(p.getBody());
    }

    @Test
    void testGetState() {
        Team team = new Team("Zeta", 1);
        Alien shooter = new Alien(team, new Position(0, 0));
        Projectile p = new Projectile(shooter, shooter.getPosition(), 0, 0, false, 0);

        assertEquals(State.FLYING, p.getState());
    }

    @Test
    void testSetState() {
        Team team = new Team("Eta", 1);
        Alien shooter = new Alien(team, new Position(0, 0));
        Projectile p = new Projectile(shooter, shooter.getPosition(), 0, 0, false, 0);

        p.setState(State.EXPLODED);
        assertEquals(State.EXPLODED, p.getState());

        p.setState(State.OUT_OF_BOUNDS);
        assertEquals(State.OUT_OF_BOUNDS, p.getState());
    }

    @Test
    void testIsActive() {
        Team team = new Team("Theta", 1);
        Alien shooter = new Alien(team, new Position(0, 0));
        Projectile p = new Projectile(shooter, shooter.getPosition(), 0, 0, false, 0);

        p.setState(State.FLYING);
        assertTrue(p.isActive());

        p.setState(State.EXPLODED);
        assertFalse(p.isActive());

        p.setState(State.OUT_OF_BOUNDS);
        assertFalse(p.isActive());
    }

    @Test
    void testIncrementFrame() {
        Team team = new Team("Iota", 1);
        Alien shooter = new Alien(team, new Position(0, 0));
        Projectile p = new Projectile(shooter, shooter.getPosition(), 0, 0, false, 0);

        p.incrementFrame();
        p.incrementFrame();
        p.incrementFrame();

        assertEquals(3, p.getFrameCount());
    }

    @Test
    void testGetFrameCount() {
        Team team = new Team("Kappa", 1);
        Alien shooter = new Alien(team, new Position(0, 0));
        Projectile p = new Projectile(shooter, shooter.getPosition(), 0, 0, false, 0);

        assertEquals(0, p.getFrameCount());
    }

}
