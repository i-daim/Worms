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
                new Position(118, 452),
                new Position(433, 158),
                new Position(591, 413),
                new Position(441, 435)
        ));


        ICE_SPAWNS.put(1, Arrays.asList(
                new Position(1254, 287),
                new Position(1047, 374),
                new Position(1398, 216),
                new Position(1586, 156)
        ));
    }

    private static final HashMap<Integer, List<Position>> POISON_SPAWNS = new HashMap<>();

    static {

        POISON_SPAWNS.put(0, Arrays.asList(
                new Position(347, 221),
                new Position(366, 465),
                new Position(546, 410),
                new Position(477, 410)
        ));


        POISON_SPAWNS.put(1, Arrays.asList(
                new Position(1252, 152),
                new Position(1411, 290),
                new Position(1367, 478),
                new Position(1285, 465)
        ));
    }

    private static final HashMap<Integer, List<Position>> HELL_SPAWNS = new HashMap<>();

    static {

        HELL_SPAWNS.put(0, Arrays.asList(
                new Position(168, 150),
                new Position(489, 190),
                new Position(290, 324),
                new Position(205, 100)
        ));


        HELL_SPAWNS.put(1, Arrays.asList(
                new Position(843, 238),
                new Position(1218, 187),
                new Position(697, 186),
                new Position(946, 395)
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
