package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upc.model.jeu.GameManager;
import upc.model.entites.Alien;
import upc.model.items.Weapon;
import upc.model.entites.Projectile;
import upc.model.jeu.GameWorld;

import static org.junit.jupiter.api.Assertions.*;

public class TestPartie {

    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager("liberty");
    }

    @Test
    void nouvellePartie_etatInitialCorrect() {
        assertNotNull(gameManager);
        assertNotNull(gameManager.getTerrain());
        Alien active = gameManager.getActiveAlien();
        assertNotNull(active);
    }

    @Test
    void testArmeCreeProjectile() {

        Alien shooter = gameManager.getActiveAlien();
        assertNotNull(shooter);

        Weapon dummyWeapon = new Weapon("TestGun", 10, 30) {
            @Override
            public double getInitialVelocity() {
                return 20;
            }

            @Override
            public boolean isAffectedByWind() {
                return false;
            }

            @Override
            public void utiliser(GameWorld world, Alien user) {
                // vide pour test
            }
        };

        Projectile projectile = dummyWeapon.createProjectile(shooter);

        assertNotNull(projectile);
        assertEquals(dummyWeapon, projectile.getWeapon());
        assertEquals(shooter, projectile.getShooter());
    }
}
