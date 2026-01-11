package upc;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;
import upc.model.jeu.TerrainGenerator;
import upc.model.physics.PhysicsEngine;

/**
 * Classe de tests dédiée à l'analyse des performances du moteur physique.
 * <p>
 * Cette classe effectue des tests de charge, d'endurance et de
 * calculs physiques lourds pour garantir la fluidité du jeu (50 FPS visés).
 * </p>
 */
public class TestPerformance {

    /** Ici le max qu'on puisse accepter c'est 20ms donc on tourne a 50fps. */
    private static final long MAX_FRAME_TIME_NS = 20_000_000;

    /** Seuil pour les explosions (plus lourd) : 10ms. */
    private static final long MAX_EXPLOSION_TIME_NS = 10_000_000;

    /** On va poser minimum 50 aliens. */
    private static final int MIN_ALIENS_REQUIRED = 50;

    /** Facteur de conversion Nano vers Millisecondes. */
    private static final double NANO_TO_MS = 1_000_000.0;

    // Constantes pour éviter les "Magic Numbers" de Checkstyle
    private static final int TERRAIN_WIDTH = 500;
    private static final int TERRAIN_HEIGHT = 300;
    private static final int WARMUP_ITERATIONS = 100;
    private static final int BATCH_SIZE = 10;
    private static final int FRAMES_TO_AVERAGE = 5;
    private static final int SAFETY_LIMIT_ALIENS = 3000;
    private static final int STABILITY_LOAD = 200;
    private static final int STABILITY_FRAMES = 1000;
    private static final int EXPLOSION_COUNT = 50;
    private static final int EXPLOSION_RADIUS = 30;

    /**
     * Test de montée en charge .
     * Identifie la limite du moteur.
     */
    @Test
    public void testMaxLoadCapacity() {
        Terrain terrain = TerrainGenerator.createEmptyTerrain(TERRAIN_WIDTH, TERRAIN_HEIGHT);
        GameWorld gameWorld = new GameWorld(null, terrain);
        PhysicsEngine physicsEngine = gameWorld.getPhysicsEngine();
        Team dummyTeam = new Team("TestTeam", 10000);

        // Ici on va initialiser le moteur physique avant de faire la simulation
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            gameWorld.update(0.0);
        }

        int alienCount = 0;
        boolean limitReached = false;
        double averageDuration = 0;

        // Boucle qui fait charger le jeu
        while (!limitReached) {

            // Ajout des aliens
            for (int i = 0; i < BATCH_SIZE; i++) {
                Position pos = new Position(100 + (i % 400), 50);
                Alien alien = new Alien(gameWorld, dummyTeam, pos);
                physicsEngine.addAlien(alien);
            }
            alienCount += BATCH_SIZE;

            // On mesure le temps total pour calculer 5 frames d'affilée
            long startTime = System.nanoTime();

            for (int k = 0; k < FRAMES_TO_AVERAGE; k++) {
                gameWorld.update(0.0);
            }

            long endTime = System.nanoTime();

            // On fait la moyenne pour avoir le temps sur une seule frame
            averageDuration = (endTime - startTime) / (double) FRAMES_TO_AVERAGE;

            if (averageDuration > MAX_FRAME_TIME_NS) {
                limitReached = true;
            }

            // Sécurité
            if (alienCount >= SAFETY_LIMIT_ALIENS) {
                break;
            }
        }

        printReport("CHARGE MAXIMALE", alienCount + " aliens", averageDuration);

        assertTrue(alienCount >= MIN_ALIENS_REQUIRED,
                "Le moteur ne supporte pas assez d'entités.");
    }

    /**
     * Test de stabilité et d'endurance.
     * Vérifie l'absence de ralentissement sur 1000 frames.
     */
    @Test
    public void testStability() {
        Terrain terrain = TerrainGenerator.createEmptyTerrain(TERRAIN_WIDTH, TERRAIN_HEIGHT);
        GameWorld gameWorld = new GameWorld(null, terrain);
        PhysicsEngine physicsEngine = gameWorld.getPhysicsEngine();
        Team dummyTeam = new Team("TestTeam", 10000);

        // On charge directement 200 aliens
        for (int i = 0; i < STABILITY_LOAD; i++) {
            Position pos = new Position(100 + (i % 400), 50);
            Alien alien = new Alien(gameWorld, dummyTeam, pos);
            physicsEngine.addAlien(alien);
        }

        // On lance 1000 tours de jeu pour voir si ca tient
        long totalTime = 0;

        for (int i = 0; i < STABILITY_FRAMES; i++) {
            long start = System.nanoTime();
            gameWorld.update(0.0);
            long end = System.nanoTime();
            totalTime += (end - start);
        }

        double averageTimePerFrame = (double) totalTime / STABILITY_FRAMES;

        printReport("STABILITÉ", STABILITY_FRAMES + " frames", averageTimePerFrame);

        assertTrue(averageTimePerFrame < MAX_FRAME_TIME_NS,
                "Le jeu est instable avec 200 aliens.");
    }

    /**
     * Test de stress sur la modification de terrain (Explosions).
     * Vérifie la vitesse de régénération des collisions.
     */
    @Test
    public void testExplosionStress() {
        Terrain terrain = TerrainGenerator.createEmptyTerrain(TERRAIN_WIDTH, TERRAIN_HEIGHT);
        GameWorld gameWorld = new GameWorld(null, terrain);

        // On va faire plein de trous dans la map
        long totalTime = 0;

        for (int i = 0; i < EXPLOSION_COUNT; i++) {
            int x = 50 + (i * 5);
            int y = 150;

            long start = System.nanoTime();

            // L'explosion modifie le terrain et recalcule la physique
            gameWorld.explode(x, y, EXPLOSION_RADIUS);
            gameWorld.update(0.0);

            long end = System.nanoTime();
            totalTime += (end - start);
        }

        double avgExplosionTime = (double) totalTime / EXPLOSION_COUNT;

        printReport("STRESS EXPLOSIONS", EXPLOSION_COUNT + " explosions", avgExplosionTime);

        // On accepte jusqu'à 10ms par explosion
        assertTrue(avgExplosionTime < MAX_EXPLOSION_TIME_NS,
                "La destruction du terrain est trop lente.");
    }

    /**
     * Affiche le rapport de test dans la console.
     * @param title Titre du test
     * @param data Donnée principale (aliens, frames...)
     * @param duration Durée moyenne en ns
     */
    private void printReport(String title, String data, double duration) {
        System.out.println("==================================================");
        System.out.println("          TEST : " + title);
        System.out.println("==================================================");
        System.out.println(" Donnée testée       : " + data);
        System.out.println(" Temps moyen         : "
                + String.format("%.3f", duration / NANO_TO_MS) + " ms");

        if (duration < MAX_FRAME_TIME_NS) {
            System.out.println(" RÉSULTAT : ✅ VALIDÉ");
        } else {
            System.out.println(" RÉSULTAT : ⚠️ ATTENTION");
        }
        System.out.println("==================================================");
    }
}