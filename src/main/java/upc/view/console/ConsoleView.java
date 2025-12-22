package upc.view.console;

import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;

import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner;
    private static final int MAX_WIDTH = 120;
    private static final int MAX_HEIGHT = 40;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void clearScreen() {
        System.out.println("\n".repeat(2));
    }

    public void printWelcome() {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║            BIENVENUE DANS WORMS (GLORB26)            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println(
                "Commandes : gauche, droite, sauter, attaquer, passer, aliens, map, exit\n");
    }

    public void printGameState(GameManager gameManager) {
        System.out.println("\n--- TOUR EN COURS ---");
        renderHUD(gameManager);
        System.out.println("💡 Tape 'map' pour afficher la carte");
    }

    protected void renderHUD(final GameManager gm) {
        Team activeTeam = gm.getActiveTeam();
        Alien activeAlien = gm.getActiveAlien();

        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════╗\n");

        String teamName = (activeTeam != null) ? activeTeam.getName() : "Aucune";
        sb.append(String.format("║ ÉQUIPE : 🏴 %-45s ║\n", teamName));

        String status = (activeAlien != null) ? "Vivant" : "Mort";
        int hp = (activeAlien != null) ? activeAlien.getHealthPoints() : 0;
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

    public int requestInteger(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("⚠️ Veuillez entrer un nombre valide : ");
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public String requestCommand(GameManager gameManager) {
        String cmd = readKey();
        if ("map".equals(cmd)) {
            printFullMap(gameManager);
            System.out.println("⏎ Appuyez sur Entrée pour continuer...");
            scanner.nextLine();
            return requestCommand(gameManager);
        }
        return cmd;
    }

    private String readKey() {
        System.out.print("\n💬 ORDRE > ");
        if (scanner.hasNextLine()) {
            return scanner.nextLine().toLowerCase().trim();
        }
        return "exit";
    }

    public void printFullMap(GameManager gameManager) {
        Terrain terrain = gameManager.getTerrain();
        if (terrain == null) {
            System.out.println("❌ Aucune carte chargée.");
            return;
        }

        int realWidth = terrain.getCollisionsWidth();
        int realHeight = terrain.getCollisionsHeight();

        int stepX = (int) Math.ceil((double) realWidth / MAX_WIDTH);
        int stepY = (int) Math.ceil((double) realHeight / MAX_HEIGHT);
        int step = Math.max(1, Math.max(stepX, stepY));

        System.out.println("\n🗺️  CARTE (1 car = " + step + "x" + step + " px) :");
        System.out.println("+" + "-".repeat(Math.min(realWidth / step, MAX_WIDTH)) + "+");

        for (int y = 0; y < realHeight; y += step) {
            System.out.print("|");
            for (int x = 0; x < realWidth; x += step) {
                char symbol = getSymbolForArea(x, y, step, gameManager);
                System.out.print(symbol);
            }
            System.out.println("|");
        }

        System.out.println("+" + "-".repeat(Math.min(realWidth / step, MAX_WIDTH)) + "+");
    }

    private char getSymbolForArea(int startX, int startY, int step, GameManager gm) {
        for (Team t : gm.getTeams()) {
            for (Alien a : t.getAliens()) {
                if (a != null && a.isAlive()) {
                    int ax = a.getPosition().getX();
                    int ay = a.getPosition().getY();
                    if (ax >= startX && ax < startX + step
                            && ay >= startY && ay < startY + step) {
                        return (a == gm.getActiveAlien()) ? '@' : 'W';
                    }
                }
            }
        }

        int centerX = startX + step / 2;
        int centerY = startY + step / 2;
        Terrain terrain = gm.getTerrain();

        if (centerX >= terrain.getCollisionsWidth()
                || centerY >= terrain.getCollisionsHeight()) {
            return ' ';
        }
        if (terrain.isWater(centerY)) {
            return '~';
        }
        if (terrain.isCollision(centerX, centerY)) {
            return '#';
        }
        return ' ';
    }

    public void close() {
        scanner.close();
    }

    public void printMovementResult(boolean b, String s, GameManager g) {
    }

    public void printJumpResult(boolean b, GameManager g) {
    }

    public void printAttackResult(Alien t, int x, int y, Team tm) {
    }

    public void printTurnSkipped() {
        System.out.println("⏭️ Tour passé.");
    }

    public void printGameAbandoned() {
        System.out.println("❌ Partie abandonnée.");
    }

    public void printUnknownCommand() {
        System.out.println("❓ Commande inconnue.");
    }

    public void printAliensStats(GameManager g) {
    }

    public void printStateAfterFall(GameManager g) {
    }

    public void printGameOver(GameManager g) {
        System.out.println("\n🏁 PARTIE TERMINÉE !");
    }
}
