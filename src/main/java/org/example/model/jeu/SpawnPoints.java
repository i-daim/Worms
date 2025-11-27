package org.example.model.jeu;

import org.example.model.entites.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SpawnPoints {
    // SPAWNS POUR LA MAP LIBERTY //
    private static final HashMap<Integer, List<Position>> libertySpawns = new HashMap<>();

    static {
        // Equipe 1
        libertySpawns.put(0, Arrays.asList(
                new Position(150, 280),  // Missile à gauche
                new Position(890, 30),   // Tête de statue
                new Position(1350, 280)  // Tank (gauche)
        ));

        // Equipe 2
        libertySpawns.put(1, Arrays.asList(
                new Position(480, 280),  // Barres electriques
                new Position(1000, 180), // Livre de statue
                new Position(1580, 280)  // Tank (droite)
        ));
    }



    // SPAWNS POUR AUTRES MAPS... //
    static {
        // Autre map
    }

}
