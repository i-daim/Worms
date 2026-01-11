package upc;

import org.junit.jupiter.api.Test;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;
import upc.model.jeu.TerrainGenerator;
import upc.model.physics.PhysicsEngine;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPerformance {

    // Ici le max qu'on puisse accepter c'est 20ms donc on tourne à 50fps
    private static final long MAX_FRAME_TIME_NS = 20_000_000;

    // On va poser minimum 50 aliens
    private static final int MIN_ALIENS_REQUIRED = 50;


}