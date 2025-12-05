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
                new Position(150, 280),  // Missile à gauche
                new Position(890, 30),   // Tête de statue
                new Position(1350, 280)  // Tank (gauche)
        ));

        // Equipe 2
        LIBERTY_SPAWNS.put(1, Arrays.asList(
                new Position(480, 280),  // Barres electriques
                new Position(800, 90),   // Tête de statue
                new Position(1580, 280)  // Tank (droite)
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
        if (mapName.isEmpty()) {
            return CONSOLE_SPAWNS.get(team);
        }
        return null;
    }
}
