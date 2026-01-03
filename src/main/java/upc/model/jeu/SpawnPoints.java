package upc.model.jeu;

import upc.model.entites.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SpawnPoints {

    // SPAWNS POUR LA MAP LIBERTY //
    private static final HashMap<Integer, List<Position>> LIBERTY_SPAWNS = new HashMap<>();

    static {
        // Equipe 1
        LIBERTY_SPAWNS.put(0, Arrays.asList(
                new Position(150, 280),   // Missile (Left)
                new Position(890, 145),    // Statue Head (Top)
                new Position(1350, 280),   // Tank (Left slope)
                new Position(960, 580)  // Statue Base (Right side/Tail)
        ));

// Equipe 2
        LIBERTY_SPAWNS.put(1, Arrays.asList(
                new Position(480, 330),   // Electric Pylon
                new Position(800, 155),    // Statue Head (Nose/Visor)
                new Position(1580, 280),  // Tank (Right side)
                new Position(60, 280)    // Far Left Beach (Edge of map)
        ));
    }

    private static final HashMap<Integer, List<Position>> SPACESHIP_SPAWNS = new HashMap<>();

    static {
        SPACESHIP_SPAWNS.put(0, Arrays.asList(
                new Position(148, 271),
                new Position(741, 522),
                new Position(924, 330),
                new Position(494, 198)
        ));

        SPACESHIP_SPAWNS.put(1, Arrays.asList(
                new Position(384, 360),
                new Position(877, 210),
                new Position(1077, 522),
                new Position(1382, 576)
        ));
    }


    // SPAWNS POUR CONSOLE //
    private static final HashMap<Integer, List<Position>> CONSOLE_SPAWNS = new HashMap<>();
    static {
        CONSOLE_SPAWNS.put(0, Arrays.asList(
                new Position(10, 5),
                new Position(12, 5),
                new Position(14, 5)
        ));

        CONSOLE_SPAWNS.put(1, Arrays.asList(
                new Position(20, 5),
                new Position(22, 5),
                new Position(24, 5)
        ));
    }



    // GETTER //
    public static List<Position> getSpawns(String mapName, int team) {
        if (mapName.equals("liberty")) {
            return LIBERTY_SPAWNS.get(team);
        }
        if (mapName.equals("spaceship")) {
            return SPACESHIP_SPAWNS.get(team);
        }
        if (mapName.isEmpty()) {
            return CONSOLE_SPAWNS.get(team);
        }
        return null;
    }
}
