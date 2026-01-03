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
                new Position(890, 145),
                new Position(1350, 280),
                new Position(960, 580)
        ));

        LIBERTY_SPAWNS.put(1, Arrays.asList(
                new Position(480, 330),
                new Position(800, 155),
                new Position(1580, 280),
                new Position(60, 280)
        ));
    }

    private static final HashMap<Integer, List<Position>> ICE_SPAWNS = new HashMap<>();

    static {
        ICE_SPAWNS.put(0, Arrays.asList(
                new Position(200, 400),
                new Position(600, 300),
                new Position(1000, 450),
                new Position(1300, 350)
        ));

        ICE_SPAWNS.put(1, Arrays.asList(
                new Position(400, 450),
                new Position(800, 250),
                new Position(1150, 400),
                new Position(1500, 300)
        ));
    }

    private static final HashMap<Integer, List<Position>> POISON_SPAWNS = new HashMap<>();

    static {
        POISON_SPAWNS.put(0, Arrays.asList(
                new Position(180, 350),
                new Position(650, 200),
                new Position(1050, 350),
                new Position(1450, 250)
        ));

        POISON_SPAWNS.put(1, Arrays.asList(
                new Position(350, 300),
                new Position(850, 450),
                new Position(1250, 250),
                new Position(1550, 400)
        ));
    }

    private static final HashMap<Integer, List<Position>> HELL_SPAWNS = new HashMap<>();

    static {
        HELL_SPAWNS.put(0, Arrays.asList(
                new Position(120, 450),
                new Position(500, 250),
                new Position(900, 500),
                new Position(1300, 300)
        ));

        HELL_SPAWNS.put(1, Arrays.asList(
                new Position(300, 350),
                new Position(700, 550),
                new Position(1100, 200),
                new Position(1500, 400)
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
        if (mapName.equals("ice")) {
            return ICE_SPAWNS.get(team);
        }
        if (mapName.equals("poison")) {
            return POISON_SPAWNS.get(team);
        }
        if (mapName.equals("hell")) {
            return HELL_SPAWNS.get(team);
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
