package upc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upc.model.entites.*;
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
        GameManager manager = new GameManager("");
        world = manager.getWorld();

        team = new Team("TestTeam", 1);
        shooter = new Alien(world, team, new Position(100, 100));
    }
}
