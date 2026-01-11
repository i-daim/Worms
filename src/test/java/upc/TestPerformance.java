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

    // Ici le max qu'on puisse accepter c'est 20ms donc on tourne a 50fps
    private static final long MAX_FRAME_TIME_NS = 20_000_000;

    // On va poser minimum 50 aliens
    private static final int MIN_ALIENS_REQUIRED = 50;

    @Test
    public void testMaxLoadCapacity() {

        Terrain terrain = TerrainGenerator.createEmptyTerrain(500, 300);
        GameWorld gameWorld = new GameWorld(null, terrain);
        PhysicsEngine physicsEngine = gameWorld.getPhysicsEngine();
        Team dummyTeam = new Team("TestTeam", 10000);

        // Ici on va initialiser le moteur physique avant de faire la simulation
        for (int i = 0; i < 100; i++) {
            gameWorld.update(0.0);
        }

        int alienCount = 0;
        int batchSize = 10;
        boolean limitReached = false;
        double averageDuration = 0;

        // Boucle qui fait charger le jeu
        while (!limitReached) {

            // Ajout des aliens
            for (int i = 0; i < batchSize; i++) {
                Position pos = new Position(100 + (i % 400), 50);
                Alien alien = new Alien(gameWorld, dummyTeam, pos);
                physicsEngine.addAlien(alien);
            }
            alienCount += batchSize;

            // On mesure le temps total pour calculer 5 frames d'affilée
            long startTime = System.nanoTime();

            for (int k = 0; k < 5; k++) {
                gameWorld.update(0.0);
            }

            long endTime = System.nanoTime();

            // On fait la moyenne pour avoir le tempss sur une seule frame
            averageDuration = (endTime - startTime) / 5.0;

            if (averageDuration > MAX_FRAME_TIME_NS) {
                limitReached = true;
            }

            if (alienCount >= 2000) { // Sécurité
                break;
            }
        }

        String failureMessage = String.format(
                "Échec Performance : Charge max = %d aliens (Moyenne Frame: %.2f ms). Attendu > %d.",
                alienCount,
                averageDuration / 1_000_000.0,
                MIN_ALIENS_REQUIRED
        );

        assertTrue(alienCount >= MIN_ALIENS_REQUIRED, failureMessage);
    }
}