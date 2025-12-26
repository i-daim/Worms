package upc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upc.model.jeu.GameManager;
import upc.model.jeu.Team;
import upc.model.save.SaveTeam;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSave {

    public Team zlerps;
    public Team glorbs;
    private SaveTeam saveTeam;
    private GameManager gameManager;

    @BeforeEach
    void setup() {
        zlerps = new Team("Zlerps", 5);
        glorbs = new Team("Glorbs", 5);
        saveTeam = new SaveTeam();
        gameManager = new GameManager();
    }

    @Test
    public void testSaveTeam() throws IOException {
        saveTeam.saveTeam(zlerps,glorbs);
        Team[] teams = saveTeam.loadTeam(gameManager);

        assertEquals(teams[0].getName(), zlerps.getName());
        //assertEquals(teams[0].getInventory(), zlerps.getInventory());
        assertEquals(teams[0].getAliens().length, zlerps.getAliens().length);
        assertEquals(teams[0].getActiveAlienIndex(), zlerps.getActiveAlienIndex());


        assertEquals(teams[1].getName(), glorbs.getName());
        //assertEquals(teams[1].getInventory(), glorbs.getInventory());
        assertEquals(teams[1].getAliens().length, glorbs.getAliens().length);
        assertEquals(teams[1].getActiveAlienIndex(), glorbs.getActiveAlienIndex());
    }
}
