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
}
