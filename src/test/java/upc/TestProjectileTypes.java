package upc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upc.model.entites.*;
import upc.model.entites.projectiles.Projectile;
import upc.model.jeu.GameManager;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Team;
import upc.model.items.*;


import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestProjectileTypes {

    private GameWorld world;
    private Team team;
    private Alien shooter;

    @BeforeEach
    public void setUp() {
        GameManager manager = new GameManager();
        world = manager.getWorld();

        team = new Team("TestTeam", 1);
        shooter = new Alien(world, team, new Position(100, 100));
    }
    @Test
    public void testWeaponShootingIntegration() {
        GameManager manager = new GameManager();
        Alien alien = new Alien(manager.getWorld(), manager.getTeams()[0], new Position(100, 100));
        Bazooka bazooka = new Bazooka();
        alien.setAimAngle(90);
        alien.setShootPower(100);


        Projectile proj = bazooka.createProjectile(alien, alien.getPosition(), alien.getAimAngle(), alien.getShootPower());
        assertEquals(alien, proj.getShooter());
    }

}
