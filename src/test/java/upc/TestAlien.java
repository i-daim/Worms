package upc;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.GameWorld;
import upc.model.jeu.GameManager;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;
import org.junit.jupiter.api.Test;

public class TestAlien {
    private Alien alien;
    private Team team;
    private GameWorld world;

    @BeforeEach
    void setup() {
        GameManager gameManager = new GameManager();
        world = gameManager.getWorld();
        team = gameManager.getTeam(0);
        alien = team.getAliens()[0];

        assertNotNull(world);
        assertNotNull(team);
        assertNotNull(alien);
    }

}