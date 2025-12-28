package upc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upc.model.entites.*;
import upc.model.jeu.GameManager;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Team;
import upc.model.items.*;

import java.awt.*;

public class TestProjectileTypes {

    private GameManager manager;
    private GameWorld world;
    private Team team;
    private Alien shooter;

    @BeforeEach
    public void setUp() {
        manager = new GameManager();
        world = manager.getWorld();

        team = new Team("TestTeam", 1);
        shooter = new Alien(world, team, new Position(100, 100));
    }

    @Test
    public void testWeaponShootingIntegration() {
        shooter.setAimAngle(90);
        shooter.setShootPower(100);

        Weapon weapon = new Uzi();
        weapon.useItem(world, shooter);
    }

}
