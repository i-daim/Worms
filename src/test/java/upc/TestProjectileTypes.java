package upc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upc.model.entites.*;
import upc.model.jeu.Team;
import upc.model.items.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestProjectileTypes {
    private Team team;
    private Alien shooter;
    @BeforeEach
    public void setUp() {
        team = new Team("TestTeam", 1);
        shooter = new Alien(team, new Position(100, 100));
    }

    @Test
    public void testStickyBomb() {

        StickyBomb weapon = new StickyBomb("Super Glue");
        Projectile projectile = weapon.createProjectile(shooter);

        assertTrue(projectile instanceof ProjectileStickyBomb, "Le projectile doit être une StickyBomb");
        ProjectileStickyBomb sticky = (ProjectileStickyBomb) projectile;

        assertFalse(sticky.isStuck(), "La StickyBomb ne doit pas être collée au départ");
        assertFalse(sticky.isReadyToExplode(), "La StickyBomb ne doit pas exploser tout de suite");

        sticky.setStuck(true);
        assertTrue(sticky.isStuck(), "La StickyBomb devrait être marquée comme collée");

        for (int i = 0; i < 180; i++) {
            assertFalse(sticky.isReadyToExplode(), "Pas encore boum à la frame " + i);
            sticky.decrementTimer();
        }

        assertTrue(sticky.isReadyToExplode(), "La StickyBomb doit exploser après 3 secondes");
    }
}
