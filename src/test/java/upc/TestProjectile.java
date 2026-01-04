package upc;

import org.junit.jupiter.api.BeforeEach;
import upc.model.jeu.GameManager;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Team;
import upc.model.entites.Position;
import upc.model.entites.Alien;
import upc.model.entites.projectiles.Projectile;
import org.junit.jupiter.api.Test;
import upc.model.entites.projectiles.Projectile.State;
import static org.junit.jupiter.api.Assertions.*;


public class TestProjectile {

    private GameWorld world;
    private Position pos = new Position(10, 20);
    private Alien shooter;

    @BeforeEach
    public void setUp() {
        GameManager manager = new GameManager();
        world = manager.getWorld();
        Team team = new Team("Alpha", 1);
        shooter = new Alien(world, team, pos);
    }

    @Test
    void testConstructor() {
        Projectile p = new Projectile(shooter, pos, 20, 0, false, 1, null);

        assertNotNull(p);
        assertEquals(10, p.getPosition().getX());
        assertEquals(20, p.getPosition().getY());
        assertEquals(1, p.getSpeed());
        assertEquals(State.FLYING, p.getState());
    }

    @Test
    void testGetShooter() {
        Projectile p = new Projectile(shooter, pos, 0, 0, false, 0, null);

        assertEquals(shooter, p.getShooter());
        assertEquals("Alpha", p.getShooter().getTeam().getName());
    }

    @Test
    void testGetBody() {
        Projectile p = new Projectile(shooter, pos, 0, 0, false, 0, null);

        assertNull(p.getBody());
    }

    @Test
    void testSetBody() {
        Projectile p = new Projectile(shooter, pos, 0, 0, false, 0, null);

        p.setBody(null);

        assertNull(p.getBody());
    }

    @Test
    void testGetState() {
        Projectile p = new Projectile(shooter, pos, 0, 0, false, 0, null);

        assertEquals(State.FLYING, p.getState());
    }

    @Test
    void testSetState() {
        Projectile p = new Projectile(shooter, pos, 0, 0, false, 0, null);

        p.setState(State.EXPLODED);
        assertEquals(State.EXPLODED, p.getState());

        p.setState(State.OUT_OF_BOUNDS);
        assertEquals(State.OUT_OF_BOUNDS, p.getState());
    }

    @Test
    void testIsActive() {
        Projectile p = new Projectile(shooter, pos, 0, 0, false, 0, null);

        p.setState(State.FLYING);
        assertTrue(p.isActive());

        p.setState(State.EXPLODED);
        assertFalse(p.isActive());

        p.setState(State.OUT_OF_BOUNDS);
        assertFalse(p.isActive());
    }

    @Test
    void testIncrementFrame() {
        Projectile p = new Projectile(shooter, pos, 0, 0, false, 0, null);

        p.incrementFrame();
        p.incrementFrame();
        p.incrementFrame();

        assertEquals(3, p.getFrameCount());
    }

    @Test
    void testGetFrameCount() {
        Projectile p = new Projectile(shooter, pos, 0, 0, false, 0, null);

        assertEquals(0, p.getFrameCount());
    }

    @Test
    public void testGetAngle() {
        Projectile p = new Projectile(shooter, pos, 0, 0, false, 0, null);
        assertEquals(0, p.getAngle());
    }

    @Test
    public void testGetExplosionRadius(){
        Projectile p = new Projectile(shooter, pos, 0, 0, false, 0, null);

        assertEquals(0, p.getExplosionRadius());
    }



}
