package upc.view.console;

import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;

import java.util.Scanner;

/**
 * Vue gérant l'interaction avec l'utilisateur via la console standard (System.in / System.out).
 * <p>
 * Cette classe est responsable de :
 * <ul>
 * <li>L'affichage des informations textuelles et des menus.</li>
 * <li>Le rendu ASCII de la carte et des listes d'aliens.</li>
 * <li>La lecture et la validation des entrées utilisateur.</li>
 * </ul>
 */
public class ConsoleView {
    private final Scanner scanner;
    private static final int MAX_WIDTH = 120;
    private static final int MAX_HEIGHT = 40;

    /**
     * Constructeur de la vue console.
     * <p>
     * Initialise le scanner pour la lecture de l'entrée standard.
     * </p>
     */
    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Simule un effacement de l'écran.
     * <p>
     * Imprime plusieurs sauts de ligne pour dégager la vue précédente.
     * </p>
     */
    public void clearScreen() {
        System.out.println("\n".repeat(2));
    }

    /**
     * Affiche la bannière de bienvenue et la liste des commandes.
     * <p>
     * Les commandes listées incluent les déplacements, les attaques et les menus d'information.
     * </p>
     */
    public void printWelcome() {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║            BIENVENUE DANS WORMS (GLORB26)            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println(
                "Commandes : gauche, droite, sauter, attaquer, passer, aliens, map, exit\n");
    }

    /**
     * Affiche le résumé de l'état du jeu pour le tour actuel.
     * <p>
     * Affiche le HUD (Heads-Up Display) et un rappel des commandes d'information.
     * </p>
     * @param gameManager Le gestionnaire de jeu.
     */
    public void printGameState(GameManager gameManager) {
        System.out.println("\n--- TOUR EN COURS ---");
        renderHUD(gameManager);
        // On n'affiche pas la liste complète à chaque tour pour ne pas spammer,
        // mais on indique qu'elle est disponible.
        System.out.println("💡 Tape 'map' pour la carte, 'aliens' pour la liste des troupes.");
    }

    /**
     * Affiche la liste détaillée des aliens avec un thème visuel par équipe.
     * <p>
     * Utilise des icônes et couleurs spécifiques pour distinguer les équipes :
     * <ul>
     * <li><b>Équipe 1 (Violet)</b> : 🟣, 💜, 👾</li>
     * <li><b>Équipe 2 (Vert)</b> : 🟢, 💚, 👽</li>
     * </ul>
     * Indique également l'état (vivant/mort) et les PV de chaque unité.
     *
     * @param gameManager Le gestionnaire de jeu contenant les équipes.
     */
    /**
     * Affiche la liste des aliens avec un thème couleur alterné (Violet/Vert).
     */
    public void printAliensList(GameManager gameManager) {
        System.out.println("\n📊 ÉTAT DES TROUPES :");

        Team[] teams = gameManager.getTeams();
        String border = "─".repeat(60);

        for (int i = 0; i < teams.length; i++) {
            Team team = teams[i];

            // Alternance des couleurs : Team 0 = Violet, Team 1 = Vert
            boolean isVioletTeam = (i % 2 == 0);

            String teamIcon = isVioletTeam ? "🟣" : "🟢";
            String heartAlive = isVioletTeam ? "💜" : "💚";
            String heartDead = "🖤";
            String alienIcon = isVioletTeam ? "👾" : "👽";

            System.out.println("┌" + border + "┐");
            System.out.printf("│ 🏴 Équipe %d : %s %-43s │\n", (i + 1),
                    teamIcon, team.getName().toUpperCase());
            System.out.println("├" + border + "┤");

            for (Alien alien : team.getAliens()) {
                if (alien != null) {
                    String statusIcon = alien.isAlive() ? alienIcon : "💀";
                    String heartIcon = alien.isAlive() ? heartAlive : heartDead;
                    String activePointer = (alien == gameManager.getActiveAlien()) ? "➤" : " ";

                    System.out.printf("│ %s %s %-10s │ 📍 (%04d, %04d) │ %s %-3d PV       │\n",
                            activePointer,
                            statusIcon,
                            "Alien",
                            alien.getPosition().getX(),
                            alien.getPosition().getY(),
                            heartIcon,
                            alien.getHealthPoints()
                    );
                }
            }
            System.out.println("└" + border + "┘");
        }
    }


    /**
     * Affiche le panneau d'information (HUD) du tour courant.
     * <p>
     * Le panneau contient :
     * <ul>
     * <li>Le nom de l'équipe active.</li>
     * <li>L'état de l'alien actif.</li>
     * <li>Une barre de vie visuelle pour l'alien actif.</li>
     * </ul>
     *
     * @param gm Le gestionnaire de jeu.
     */
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

    /**
     * Génère une barre de progression textuelle pour les points de vie.
     *
     * @param hp Les points de vie (0-100).
     * @return Une chaîne représentant la barre (ex: <code>[█████░░░░░]</code>).
     */
    private String generateHealthBar(int hp) {
        int bars = Math.max(0, Math.min(10, hp / 10));
        return "[" + "█".repeat(bars) + "░".repeat(10 - bars) + "]";
    }

    /**
     * Demande à l'utilisateur de saisir un nombre entier.
     * <p>
     * La méthode boucle tant que l'utilisateur ne saisit pas un entier valide.
     * </p>
     * @param message Le message d'invite à afficher.
     * @return L'entier saisi par l'utilisateur.
     */
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

    /**
     * Demande une commande à l'utilisateur et gère les commandes d'information en interne.
     * <p>
     * Si l'utilisateur tape "map" ou "aliens", la vue affiche l'information demandée
     * puis redemande une commande (récursivement), sans passer la main au contrôleur.
     * </p>
     * @param gameManager Le gestionnaire de jeu pour récupérer les infos à afficher.
     * @return La commande d'action finale (déplacement, attaque, etc.).
     */
    // Modification ici pour gérer la commande "aliens"
    public String requestCommand(GameManager gameManager) {
        String cmd = readKey();

        if ("map".equals(cmd)) {
            printFullMap(gameManager);
            waitForEnter();
            return requestCommand(gameManager);
        }

        if ("aliens".equals(cmd)) {
            printAliensList(gameManager);
            waitForEnter();
            return requestCommand(gameManager);
        }

        return cmd;
    }

    /**
     * Met le programme en pause jusqu'à ce que l'utilisateur appuie sur Entrée.
     */
    private void waitForEnter() {
        System.out.println("⏎ Appuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    /**
     * Lit une ligne de texte depuis la console.
     *
     * @return La chaîne saisie en minuscules et sans espaces superflus, ou "exit" en cas d'erreur.
     */
    private String readKey() {
        System.out.print("\n💬 ORDRE > ");
        if (scanner.hasNextLine()) {
            return scanner.nextLine().toLowerCase().trim();
        }
        return "exit";
    }

    /**
     * Affiche une représentation ASCII de la carte complète du jeu.
     * <p>
     * La carte est redimensionnée (downscaling) si elle dépasse les dimensions maximales (MAX_WIDTH/MAX_HEIGHT).
     * Les symboles affichés sont déterminés par {@link #getSymbolForArea}.
     * </p>
     * @param gameManager Le gestionnaire de jeu contenant le terrain.
     */
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

    /**
     * Détermine le symbole à afficher pour une zone donnée de la carte (gestion du zoom/échelle).
     * <p>
     * Priorité d'affichage :
     * <ol>
     * <li><b>@</b> : Alien actif</li>
     * <li><b>W</b> : Alien vivant</li>
     * <li><b>~</b> : Eau</li>
     * <li><b>#</b> : Collision / Terre</li>
     * <li>(Espace) : Vide</li>
     * </ol>
     *
     * @param startX Coordonnée X de début de la zone.
     * @param startY Coordonnée Y de début de la zone.
     * @param step Taille de la zone (pas d'échantillonnage).
     * @param gm Le gestionnaire de jeu.
     * @return Le caractère représentant la zone.
     */
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

    /**
     * Ferme les ressources associées à la vue (Scanner).
     */
    public void close() {
        scanner.close();
    }

    /**
     * Méthode vide destinée à être surchargée ou implémentée si besoin d'afficher le résultat d'un mouvement.
     */
    public void printMovementResult(boolean b, String s, GameManager g) {
    }

    /**
     * Méthode vide destinée à être surchargée ou implémentée si besoin d'afficher le résultat d'un saut.
     */
    public void printJumpResult(boolean b, GameManager g) {
    }

    /**
     * Méthode vide destinée à être surchargée ou implémentée si besoin d'afficher le résultat d'une attaque.
     */
    public void printAttackResult(Alien t, int x, int y, Team tm) {
    }

    /**
     * Affiche un message indiquant que le tour a été passé.
     */
    public void printTurnSkipped() {
        System.out.println("⏭️ Tour passé.");
    }

    /**
     * Affiche un message indiquant que la partie a été abandonnée.
     */
    public void printGameAbandoned() {
        System.out.println("❌ Partie abandonnée.");
    }

    /**
     * Affiche un message d'erreur pour une commande inconnue.
     */
    public void printUnknownCommand() {
        System.out.println("❓ Commande inconnue.");
    }

    /**
     * Méthode vide destinée à l'affichage des stats des aliens.
     * (Fonctionnalité gérée par {@link #printAliensList(GameManager)} dans cette vue).
     */
    public void printAliensStats(GameManager g) {
    }

    /**
     * Affiche le message de fin de partie.
     *
     * @param g Le gestionnaire de jeu.
     */
    public void printGameOver(GameManager g) {
        System.out.println("\n🏁 PARTIE TERMINÉE !");
    }
}