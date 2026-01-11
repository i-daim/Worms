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

            // On fait la moyenne pour avoir le temps sur une seule frame
            averageDuration = (endTime - startTime) / 5.0;

            if (averageDuration > MAX_FRAME_TIME_NS) {
                limitReached = true;
            }

            if (alienCount >= 3000) {
                break;
            }
        }

        printReport("CHARGE MAXIMALE", alienCount, averageDuration);

        assertTrue(alienCount >= MIN_ALIENS_REQUIRED, "Le moteur ne supporte pas assez d'entités.");
    }

    @Test
    public void testStability() {
        Terrain terrain = TerrainGenerator.createEmptyTerrain(500, 300);
        GameWorld gameWorld = new GameWorld(null, terrain);
        PhysicsEngine physicsEngine = gameWorld.getPhysicsEngine();
        Team dummyTeam = new Team("TestTeam", 10000);

        // On charge directement 200 aliens
        int fixedLoad = 200;
        for (int i = 0; i < fixedLoad; i++) {
            Position pos = new Position(100 + (i % 400), 50);
            Alien alien = new Alien(gameWorld, dummyTeam, pos);
            physicsEngine.addAlien(alien);
        }

        // Test sur la durée (1000 tours de jeu)
        long totalTime = 0;
        int frames = 1000;

        for (int i = 0; i < frames; i++) {
            long start = System.nanoTime();
            gameWorld.update(0.0);
            long end = System.nanoTime();
            totalTime += (end - start);
        }

        double averageTimePerFrame = (double) totalTime / frames;

        System.out.println("==================================================");
        System.out.println("          TEST DE STABILITÉ (ENDURANCE)           ");
        System.out.println("==================================================");
        System.out.println(" Charge fixe         : " + fixedLoad + " aliens");
        System.out.println(" Frames simulées     : " + frames);
        System.out.println(" Temps moyen/frame   : " + String.format("%.3f", averageTimePerFrame / 1_000_000.0) + " ms");
        System.out.println("==================================================");

        assertTrue(averageTimePerFrame < 16_600_000, "Le jeu est instable ou trop lent avec 200 aliens.");
    }

    private void printReport(String title, int count, double duration) {
        System.out.println("\n==================================================");
        System.out.println("          TEST : " + title);
        System.out.println("==================================================");
        System.out.println(" Limite atteinte à   : " + count + " aliens");
        System.out.println(" Temps par frame     : " + String.format("%.3f", duration / 1_000_000.0) + " ms");

        if (count >= MIN_ALIENS_REQUIRED) {
            System.out.println(" RÉSULTAT : ✅ VALIDÉ");
        } else {
            System.out.println(" RÉSULTAT : ❌ ÉCHEC");
        }
        System.out.println("==================================================\n");
    }
}