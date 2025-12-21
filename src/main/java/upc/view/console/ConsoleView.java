package upc.view.console;

import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;

import java.util.Scanner;

/**
 * Console view for Worms (Glorb26).
 * Handles buffered display and user input.
 */
public class ConsoleView {
    private final Scanner scanner;

    // Game window size
    private static final int MAX_WIDTH = 60;
    private static final int MAX_HEIGHT = 25;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Clears the screen (simulated with line breaks to avoid IDE bugs).
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
     * Displays the full game state (HUD + Terrain).
     */
    public void printGameState(GameManager gameManager) {
        System.out.println("\n--- TOUR EN COURS ---");
        renderHUD(gameManager);
        printTerrain(gameManager);
    }

    /**
     * Renders the Head-Up Display (HUD) with stats.
     */
    protected void renderHUD(final GameManager gm) {
        Team activeTeam = gm.getActiveTeam();
        Alien activeAlien = gm.getActiveAlien();

        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════╗\n");

        // Team Line
        String teamName = (activeTeam != null) ? activeTeam.getName() : "Aucune";
        sb.append(String.format("║ ÉQUIPE : 🏴 %-45s ║\n", teamName));

        // Alien & Health Line
        String status = (activeAlien != null) ? "Vivant" : "Mort";
        int hp = (activeAlien != null) ? activeAlien.getHealthPoints() : 0;

        // Visual Health Bar [████░░░░░░]
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
     * Renders the terrain map in ASCII.
     */
    public void printTerrain(GameManager gameManager) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n🗺️  CARTE (EAU Y=").append(Terrain.getWaterLevel()).append("):\n");

        // Top Border
        sb.append("╔");
        for(int i = 0; i < MAX_WIDTH; i++) sb.append("═");
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

        // Bottom Border
        sb.append("╚");
        for(int i = 0; i < MAX_WIDTH; i++) sb.append("═");
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

    // --- Input Methods ---

    public String readKey() {
        System.out.print("\n💬 ORDRE > ");
        if (scanner.hasNextLine()) {
            return scanner.nextLine().toLowerCase().trim();
        }
        return "exit";
    }

    public void close() { scanner.close(); }

    // --- Mock Methods for compatibility with current Controller ---
    // (These prevent compilation errors until you update the Controller in Ticket 3)

    public void printMovementResult(boolean b, String s, GameManager g) {}
    public void printJumpResult(boolean b, GameManager g) {}
    public void printAttackResult(Alien t, int x, int y, Team tm) {}
    public void printTurnSkipped() {}
    public void printGameAbandoned() {}
    public void printUnknownCommand() {}
    public void printAliensStats(GameManager g) {}
    public void printStateAfterFall(GameManager g) {}
    public void printGameOver(GameManager g) {}
    public int chooseDamage() { return 10; }
    public int chooseX() { return 0; }
    public int chooseY() { return 0; }
}
