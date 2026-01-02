package upc.model.jeu;

import upc.model.entites.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SpawnPoints {

    private static final HashMap<Integer, List<Position>> LIBERTY_SPAWNS = new HashMap<>();

    static {
        LIBERTY_SPAWNS.put(0, Arrays.asList(
                new Position(150, 280),
                new Position(890, 30),
                new Position(1350, 280)
        ));

        LIBERTY_SPAWNS.put(1, Arrays.asList(
                new Position(480, 280),
                new Position(800, 90),
                new Position(1580, 280)
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

    private static final HashMap<Integer, List<Position>> ICE_SPAWNS = new HashMap<>();

    static {
        // Equipe 1
        ICE_SPAWNS.put(0, Arrays.asList(
                new Position(424, 142),
                new Position(132, 453),
                new Position(607, 413),
                new Position(446, 442)
        ));

        // Equipe 2
        ICE_SPAWNS.put(1, Arrays.asList(
                new Position(1151, 254),
                new Position(1584, 149),
                new Position(1390, 204),
                new Position(1042, 375)
        ));
    }

    // SPAWNS POUR LA MAP POISON //
    private static final HashMap<Integer, List<Position>> POISON_SPAWNS = new HashMap<>();

    static {
        // Equipe 1
        POISON_SPAWNS.put(0, Arrays.asList(
                new Position(385, 235),
                new Position(375, 464),
                new Position(281, 450),
                new Position(541, 410)
        ));

        // Equipe 2
        POISON_SPAWNS.put(1, Arrays.asList(
                new Position(1431, 285),
                new Position(1253, 151),
                new Position(1302, 469),
                new Position(1539, 348)
        ));
    }





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

    public static List<Position> getSpawns(String mapName, int team) {
        if (mapName.equals("liberty")) {
            return LIBERTY_SPAWNS.get(team);
        }
        if (mapName.equals("spaceship")) {
            return SPACESHIP_SPAWNS.get(team);
        }
        if (mapName.equals("ice")) {
            return ICE_SPAWNS.get(team);
        }
        if (mapName.isEmpty()) {
            return CONSOLE_SPAWNS.get(team);
        }
        return LIBERTY_SPAWNS.get(team);
    }
}
