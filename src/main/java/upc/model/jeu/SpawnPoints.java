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


    /// SPAWNS POUR CONSOLE ///

    /**Constante représentant l'ensemble des positions de départ des alien de chaque équipe pour le terrain de jeu Console*/
    private static final HashMap<Integer, List<Position>> CONSOLE_SPAWNS = new HashMap<>();

    /**Initialise la position de départ des aliens de chaque équipe
     * sur le terrain de jeu Console
     */
    static {

        //Equipe 1
        CONSOLE_SPAWNS.put(0, Arrays.asList(
                new Position(10, 5),
                new Position(12, 5),
                new Position(14, 5)
        ));

        //Equipe 2
        CONSOLE_SPAWNS.put(1, Arrays.asList(
                new Position(20, 5),
                new Position(22, 5),
                new Position(24, 5)
        ));
    }



    /// GETTER ///

    /**
     * Retourne l'ensemble des positions des aliens selon le terrain et l'équipe choisi
     * @param mapName : Nom du terrain
     * @param team : Indice d'équipe
     * @return la liste de position {@link List<Position>} des aliens d'une équipe
     * si le nom de terrain choisi est liberty, spaceship ou vide
     * ou {@code null} si le nom du terrain n'est pas reconnu
     */
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
