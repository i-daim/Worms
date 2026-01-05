package upc.model.jeu;

import upc.model.entites.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SpawnPoints {

    /// SPAWNS POUR LA MAP LIBERTY ///

    /**
     * Constante représentant l'ensemble des positions de départ des alien de chaque équipe
     * pour le terrain de jeu Liberty
     */
    private static final HashMap<Integer, List<Position>> LIBERTY_SPAWNS = new HashMap<>();

    /**
     * Initialise la position de départ des aliens de chaque équipe
     * sur le terrain de jeu Liberty
     */
    static {

        //Equipe 1
        LIBERTY_SPAWNS.put(0, Arrays.asList(
                new Position(150, 280),
                new Position(890, 145),
                new Position(1350, 280),
                new Position(960, 580)
        ));

        //Equipe 2
        LIBERTY_SPAWNS.put(1, Arrays.asList(
                new Position(480, 330),
                new Position(800, 155),
                new Position(1580, 280),
                new Position(60, 280)
        ));
    }

    /// SPAWNS POUR LA MAP ICE ///

    /**
     * Constante représentant l'ensemble des positions de départ des alien de chaque équipe
     * pour le terrain de jeu Ice
     */
    private static final HashMap<Integer, List<Position>> ICE_SPAWNS = new HashMap<>();

    /**
     * Initialise la position de départ des aliens de chaque équipe
     * sur le terrain de jeu Ice
     */
    static {

        //Equipe 1
        ICE_SPAWNS.put(0, Arrays.asList(
                new Position(118, 452),
                new Position(433, 158),
                new Position(591, 413),
                new Position(441, 435)
        ));

        //Equipe 2
        ICE_SPAWNS.put(1, Arrays.asList(
                new Position(1254, 287),
                new Position(1047, 374),
                new Position(1398, 216),
                new Position(1586, 156)
        ));
    }


    /// SPAWNS POUR LA MAP POISON ///

    /**
     * Constante représentant l'ensemble des positions de départ des alien de chaque équipe
     * pour le terrain de jeu Poison
     */
    private static final HashMap<Integer, List<Position>> POISON_SPAWNS = new HashMap<>();


    /**
     * Initialise la position de départ des aliens de chaque équipe
     * sur le terrain de jeu Poison
     */
    static {

        //Equipe 1
        POISON_SPAWNS.put(0, Arrays.asList(
                new Position(347, 221),
                new Position(366, 465),
                new Position(546, 410),
                new Position(477, 410)
        ));


        //Equipe 2
        POISON_SPAWNS.put(1, Arrays.asList(
                new Position(1252, 152),
                new Position(1411, 290),
                new Position(1367, 478),
                new Position(1285, 465)
        ));
    }

    /// SPAWNS POUR LA MAP HELL ///

    /**
     * Constante représentant l'ensemble des positions de départ des alien de chaque équipe
     * pour le terrain de jeu Hell
     */
    private static final HashMap<Integer, List<Position>> HELL_SPAWNS = new HashMap<>();


    /**
     * Initialise la position de départ des aliens de chaque équipe
     * sur le terrain de jeu Hell
     */
    static {

        //Equipe 1
        HELL_SPAWNS.put(0, Arrays.asList(
                new Position(168, 150),
                new Position(489, 190),
                new Position(290, 324),
                new Position(205, 100)
        ));

        //Equipe 2
        HELL_SPAWNS.put(1, Arrays.asList(
                new Position(843, 238),
                new Position(1218, 187),
                new Position(697, 186),
                new Position(946, 395)
        ));
    }

    /// SPAWNS POUR LA MAP SPACESHIP ///

    /**
     * Constante représentant l'ensemble des positions de départ des alien de chaque équipe
     * pour le terrain de jeu Spaceship
     */
    private static final HashMap<Integer, List<Position>> SPACESHIP_SPAWNS = new HashMap<>();

    /**
     * Initialise la position de départ des aliens de chaque équipe
     * sur le terrain de jeu Spaceship
     */
    static {

        //Equipe 1
        SPACESHIP_SPAWNS.put(0, Arrays.asList(
                new Position(148, 271),
                new Position(741, 522),
                new Position(924, 330),
                new Position(494, 198)
        ));

        //Equipe 2
        SPACESHIP_SPAWNS.put(1, Arrays.asList(
                new Position(384, 360),
                new Position(877, 210),
                new Position(1077, 522),
                new Position(1382, 576)
        ));
    }


    /// SPAWNS POUR CONSOLE ///

    /**
     * Constante représentant l'ensemble des positions de départ des alien de chaque équipe
     * pour le terrain de jeu Console
     */
    private static final HashMap<Integer, List<Position>> CONSOLE_SPAWNS = new HashMap<>();

    /**
     * Initialise la position de départ des aliens de chaque équipe
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
     * si le nom de terrain choisi est liberty, spaceship, ice, poison, hell ou vide
     * ou {@code null} si le nom du terrain n'est pas reconnu
     */
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
