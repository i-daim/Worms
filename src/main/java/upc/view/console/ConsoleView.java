package upc.view.console;

import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.Team;
import upc.model.jeu.GameManager;
import upc.model.jeu.Terrain;

import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void printWelcome() {
        System.out.println("=================================");
        System.out.println("  Bienvenue dans Worms Game!    ");
        System.out.println("=================================");
        System.out.println("Commandes disponibles:");
        System.out.println("  gauche  - Déplacer l'alien à gauche");
        System.out.println("  droite  - Déplacer l'alien à droite");
        System.out.println("  sauter  - Faire sauter l'alien");
        System.out.println("  dégâts  - Infliger des dégâts à l'équipe ennemie");
        System.out.println("  passer  - Passer son tour");
        System.out.println("  aliens  - Afficher tous les aliens vivants");
        System.out.println("  map     - Afficher la carte du terrain");
        System.out.println("  info    - Afficher l'état complet de la partie");
        System.out.println("  exit    - Quitter la partie");
        System.out.println("=================================\n");
    }

    public void printGameState(GameManager gameManager) {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("📊 " + gameManager.toString());
        System.out.println("─".repeat(60));

        renderHUD(gameManager);
        System.out.println("─".repeat(60));
    }

    /**
     * Affiche le panneau d'informations (HUD).
     * @param gm le gestionnaire de jeu
     */

    protected void renderHUD(GameManager gm) {
        Team equipeActive = gm.getActiveTeam();
        Alien alienActif = gm.getActiveAlien();

        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════╗\n");
        sb.append(String.format("║ EQUIPE : [E] %-20s                ║\n", equipeActive.getName()));
        String statut = (alienActif != null) ? "Vivant" : "Mort";
        sb.append(String.format("║ ALIEN  : [A] %-20s                ║\n", statut));
        int pv = (alienActif != null) ? alienActif.getHealthPoints() : 0;
        sb.append(String.format("║ SANTE  : [+] %-3d PV                            ║\n", pv));
        sb.append("╚══════════════════════════════════════════════════════╝");

        System.out.println(sb.toString());
    }



    public void printAliensStats(GameManager gameManager) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("           🐛 ÉTAT DE TOUS LES ALIENS VIVANTS 🐛");
        System.out.println("═".repeat(60));

        Team teamActive = gameManager.getActiveTeam();
        Team teamEnnemie = gameManager.getEnnemyTeam();

        System.out.println("\n💚 ÉQUIPE ALLIÉE : " + teamActive.getName());
        System.out.println("─".repeat(60));
        printAliensTeam(teamActive, gameManager.getActiveAlien());

        System.out.println("\n💀 ÉQUIPE ENNEMIE : " + teamEnnemie.getName());
        System.out.println("─".repeat(60));
        printAliensTeam(teamEnnemie, null);

        System.out.println("═".repeat(60) + "\n");
    }

    private void printAliensTeam(Team team, Alien alienActif) {
        Alien[] aliens = team.getAliens();
        int count = 1;
        boolean noneAlive = true;

        for (Alien alien : aliens) {
            if (alien != null && alien.isAlive()) {
                noneAlive = false;
                Position pos = alien.getPosition();
                String indicateur = (alien == alienActif) ? "► " : "  ";

                System.out.printf("%sAlien #%d | Position: %s | PV: %3d/100 ",
                        indicateur, count, pos, alien.getHealthPoints());
                printHealthBar(alien.getHealthPoints());
                count++;
            }
        }

        if (noneAlive) {
            System.out.println("  ⚰️  Tous les aliens sont morts");
        }
    }


    private void printHealthBar(int pv) {
        int barres = pv / 10;
        System.out.print("[");
        for (int i = 0; i < 10; i++) {
            if (i < barres) {
                System.out.print("█");
            } else {
                System.out.print("░");
            }
        }
        System.out.println("]");
    }

    public void printGameOver(GameManager gameManager) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("            🎉 PARTIE TERMINÉE ! 🎉");
        System.out.println("=".repeat(60));

        Team gagnante = gameManager.getWinningTeam();
        if (gagnante != null) {
            System.out.println("\n🏆 " + gagnante.getName() + " a gagné ! 🏆\n");
        }

        renderHUD(gameManager);
        System.out.println("\n" + "=".repeat(60));
    }

    public void printTerrain(GameManager gameManager) {
        Terrain terrain = gameManager.getTerrain();
        System.out.println("\n🗺️  Carte (ligne d'eau Y=" + Terrain.getWaterLevel() + "):");

        int height = Math.min(terrain.getCollisionsHeight(), 25);
        int width = Math.min(terrain.getCollisionsWidth(), 60);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(symbolForTile(x, y, gameManager));
            }
            System.out.println();
        }
        System.out.println();
    }

    public char symbolForTile(int x, int y, GameManager gameManager) {
        Alien w = gameManager.getAlienAtPosition(x, y);
        Terrain terrain = gameManager.getTerrain();
        if (w != null && w.isAlive()) {
            return 'W';
        }
        if (terrain.isCollision(x, y)) {
            return 'X';
        }
        if (terrain.isWater(y) && terrain.isCollision(x, y)) {
            return '~';
        }
        return ' ';
    }

    public void printStateAfterFall(GameManager gameManager) {
        System.out.println("\n💥 ÉTAT DES ALIENS APRÈS LA CHUTE :\n");

        for (int i = 0; i < 2; i++) {
            System.out.println("🔹 " + gameManager.getTeam(i).getName() + " :");

            Alien[] aliens = gameManager.getTeam(i).getAliens();
            for (int j = 0; j < aliens.length; j++) {
                Alien a = aliens[j];
                if (a != null && a.isAlive()) {
                    System.out.println("   → Alien #" + (j + 1) + " : PV = "
                            + a.getHealthPoints() + "/100");
                }
            }
        }
        System.out.println();
    }


    public void printMovementResult(boolean success, String direction) {
        if (success) {
            System.out.println("✅ Déplacement à " + direction + " réussi");
        } else {
            System.out.println("❌ Déplacement impossible (collision ou hors limites)");
        }
    }

    public void printJumpResult(boolean success) {
        if (success) {
            System.out.println("✅ L'alien a sauté !");
        } else {
            System.out.println("❌ Saut impossible");
        }
    }

    public void printAttackResult(Alien target, int x, int y, Team activeTeam) {
        if (target == null) {
            System.out.println("❌ Aucun alien à la position (" + x + ", " + y + ")");
            return;
        }

        boolean isAlly = (target.getTeam() == activeTeam);
        boolean alienDead = !target.isAlive();

        if (isAlly) {
            if (alienDead) {
                System.out.println("💀 TIR AMI FATAL ! Vous avez tué votre propre alien !");
            } else {
                System.out.println("⚠️  TIR AMI ! Votre alien a été touché (PV restants: "
                        + target.getHealthPoints() + ")");

            }
        } else {
            if (alienDead) {
                System.out.println("💀 Alien ennemi éliminé !");
            } else {
                System.out.println("💥 Alien ennemi touché ! Il lui reste "
                        + target.getHealthPoints() + " PV");
            }
        }
    }

    public void printTurnSkipped() {
        System.out.println("⏭️  Tour passé");
    }

    public void printGameAbandoned() {
        System.out.println("\n❌ Partie abandonnée.");
    }

    public void printUnknownCommand() {
        System.out.println("❌ Commande inconnue ! Tapez 'info' pour l'aide.");
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public String requestCommand() {
        System.out.print("\n💬 > ");
        return scanner.nextLine().toLowerCase().trim();
    }

    public int requestInteger(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("⚠️  Veuillez entrer un nombre valide: ");
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public void close() {
        scanner.close();
        System.out.println("\n👋 Merci d'avoir joué !");
    }
}
