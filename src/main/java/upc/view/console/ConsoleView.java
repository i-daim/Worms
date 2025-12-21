package upc.view.console;

import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;

import java.util.Scanner;

/**
 * Vue console pour Worms (Glorb26).
 * Gère l'affichage bufferisé et les entrées utilisateur.
 */
public class ConsoleView {
    private final Scanner scanner;

    // Taille de la fenêtre de jeu
    private static final int MAX_WIDTH = 60;
    private static final int MAX_HEIGHT = 25;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Nettoie l'écran (simulé avec des sauts de ligne pour éviter les bugs d'IDE).
     */
    public void clearScreen() {
        System.out.println("\n".repeat(2));
    }

    public void printWelcome() {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║            BIENVENUE DANS WORMS (GLORB26)            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("Commandes : gauche, droite, sauter, attaquer, passer, exit\n");
    }

    /**
     * Affiche l'état complet du jeu (HUD + Terrain).
     */
    public void printGameState(GameManager gameManager) {
        System.out.println("\n--- TOUR EN COURS ---");
        renderHUD(gameManager);
        printTerrain(gameManager);
    }

    /**
     * Affiche le bandeau d'informations (HUD) avec les statistiques.
     */
    protected void renderHUD(final GameManager gm) {
        Team activeTeam = gm.getActiveTeam();
        Alien activeAlien = gm.getActiveAlien();

        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════╗\n");

        // Ligne Équipe
        String teamName = (activeTeam != null) ? activeTeam.getName() : "Aucune";
        sb.append(String.format("║ ÉQUIPE : 🏴 %-45s ║\n", teamName));

        // Ligne Alien & Santé
        String status = (activeAlien != null) ? "Vivant" : "Mort";
        int hp = (activeAlien != null) ? activeAlien.getHealthPoints() : 0;

        // Barre de vie visuelle [████░░░░░░]
        String healthBar = generateHealthBar(hp);

        sb.append(String.format("║ ALIEN  : 👽 %-10s | SANTÉ : ❤️ %03d %-16s ║\n",
                status, hp, healthBar));

        sb.append("╚══════════════════════════════════════════════════════════╝");
        System.out.println(sb.toString());
    }

    private String generateHealthBar(int hp) {
        int bars = Math.max(0, Math.min(10, hp / 10));
        return "[" + "█".repeat(bars) + "░".repeat(10 - bars) + "]";
    }

    /**
     * Affiche la carte du terrain en ASCII.
     */
    public void printTerrain(GameManager gameManager) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n🗺️  CARTE (EAU Y=").append(Terrain.getWaterLevel()).append("):\n");

        // Bordure haute
        sb.append("╔");
        for (int i = 0; i < MAX_WIDTH; i++) {
            sb.append("═");
        }
        sb.append("╗\n");

        Terrain terrain = gameManager.getTerrain();
        int height = Math.min(terrain.getCollisionsHeight(), MAX_HEIGHT);
        int width = Math.min(terrain.getCollisionsWidth(), MAX_WIDTH);

        for (int y = 0; y < height; y++) {
            sb.append("║");
            for (int x = 0; x < width; x++) {
                sb.append(calculateTileChar(x, y, gameManager));
            }
            sb.append("║\n");
        }

        // Bordure basse
        sb.append("╚");
        for (int i = 0; i < MAX_WIDTH; i++) {
            sb.append("═");
        }
        sb.append("╝\n");

        System.out.println(sb.toString());
    }

    private char calculateTileChar(int x, int y, GameManager gm) {
        Alien w = gm.getAlienAtPosition(x, y);
        if (w != null && w.isAlive()) {
            return 'Ω';
        }
        if (gm.getTerrain().isCollision(x, y)) {
            return '█';
        }
        if (gm.getTerrain().isWater(y)) {
            return '≈';
        }
        return '░';
    }

    // --- Méthodes d'entrée ---

    public String readKey() {
        System.out.print("\n💬 ORDRE > ");
        if (scanner.hasNextLine()) {
            return scanner.nextLine().toLowerCase().trim();
        }
        return "exit";
    }

    public void close() {
        scanner.close();
    }

    // --- Méthodes "Mock" pour la compatibilité ---

    public void printMovementResult(boolean b, String s, GameManager g) {
        // Mock
    }

    public void printJumpResult(boolean b, GameManager g) {
        // Mock
    }

    public void printAttackResult(Alien t, int x, int y, Team tm) {
        // Mock
    }

    public void printTurnSkipped() {
        // Mock
    }

    public void printGameAbandoned() {
        // Mock
    }

    public void printUnknownCommand() {
        // Mock
    }

    public void printAliensStats(GameManager g) {
        // Mock
    }

    public void printStateAfterFall(GameManager g) {
        // Mock
    }

    public void printGameOver(GameManager g) {
        // Mock
    }

    public int chooseDamage() {
        return 10;
    }

    public int chooseX() {
        return 0;
    }

    public int chooseY() {
        return 0;
    }
}
