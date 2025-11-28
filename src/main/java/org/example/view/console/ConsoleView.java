package org.example.view.console;

import org.example.model.entites.Alien;
import org.example.model.entites.Position;
import org.example.model.jeu.Team;
import org.example.model.jeu.GameManager;
import org.example.model.jeu.Terrain;

import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void afficherBienvenue() {
        System.out.println("=================================");
        System.out.println("  Bienvenue dans Worms Game!    ");
        System.out.println("=================================");
        System.out.println("Commandes disponibles:");
        System.out.println("  gauche  - Déplacer le worm à gauche");
        System.out.println("  droite  - Déplacer le worm à droite");
        System.out.println("  sauter  - Faire sauter le worm");
        System.out.println("  degats  - Infliger des dégâts à l'équipe ennemie");
        System.out.println("  passer  - Passer son tour");
        System.out.println("  worms   - Afficher tous les worms vivants");
        System.out.println("  map     - Afficher la carte du terrain");
        System.out.println("  info    - Afficher l'état complet de la partie");
        System.out.println("  exit    - Quitter la partie");
        System.out.println("=================================\n");
    }

    public void afficherEtatPartie(GameManager gameManager) {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("📊 " + gameManager.toString());
        System.out.println("─".repeat(60));

        afficherStatistiquesEquipes(gameManager);
        afficherWormActif(gameManager);

        System.out.println("─".repeat(60));
    }

    private void afficherStatistiquesEquipes(GameManager gameManager) {
        System.out.println("\n🏆 État des équipes:");

        Team[] teams = gameManager.getTeams();
        for (int i = 0; i < teams.length; i++) {
            String indicateur = (i == gameManager.getActiveTeamIndex()) ? "►" : " ";
            System.out.println(indicateur + "  " + teams[i].toString());
        }
    }

    private void afficherWormActif(GameManager gameManager) {
        Alien alien = gameManager.getActiveWorm();

        if (alien != null) {
            Position pos = alien.getPosition();
            System.out.println("\n🐛 Worm actif (" + gameManager.getActiveTeam().getName() + "):");
            System.out.printf("   Position: %s\n", pos);
            System.out.printf("   PV: %d/100 ", alien.getHealthPoints());
            afficherBarreVie(alien.getHealthPoints());
        } else {
            System.out.println("\n⚠️  Aucun worm actif");
        }
    }

    public void afficherTousLesWorms(GameManager gameManager) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("           🐛 ÉTAT DE TOUS LES WORMS VIVANTS 🐛");
        System.out.println("═".repeat(60));

        Team teamActive = gameManager.getActiveTeam();
        Team teamEnnemie = gameManager.getEnnemyTeam();

        System.out.println("\n💚 ÉQUIPE ALLIÉE : " + teamActive.getName());
        System.out.println("─".repeat(60));
        afficherWormsEquipe(teamActive, gameManager.getActiveWorm());

        System.out.println("\n💀 ÉQUIPE ENNEMIE : " + teamEnnemie.getName());
        System.out.println("─".repeat(60));
        afficherWormsEquipe(teamEnnemie, null);

        System.out.println("═".repeat(60) + "\n");
    }

    private void afficherWormsEquipe(Team team, Alien alienActif) {
        Alien[] aliens = team.getWorms();
        int count = 1;
        boolean aucunVivant = true;

        for (Alien alien : aliens) {
            if (alien != null && alien.isAlive()) {
                aucunVivant = false;
                Position pos = alien.getPosition();
                String indicateur = (alien == alienActif) ? "► " : "  ";

                System.out.printf("%sWorm #%d | Position: %s | PV: %3d/100 ",
                        indicateur, count, pos, alien.getHealthPoints());
                afficherBarreVie(alien.getHealthPoints());
                count++;
            }
        }

        if (aucunVivant) {
            System.out.println("  ⚰️  Tous les worms sont morts");
        }
    }


    private void afficherBarreVie(int pv) {
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

    public void afficherFinPartie(GameManager gameManager) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("            🎉 PARTIE TERMINÉE ! 🎉");
        System.out.println("=".repeat(60));

        Team gagnante = gameManager.getWinningTeam();
        if (gagnante != null) {
            System.out.println("\n🏆 " + gagnante.getName() + " a gagné ! 🏆\n");
        }

        afficherStatistiquesEquipes(gameManager);
        System.out.println("\n" + "=".repeat(60));
    }

    public void afficherTerrain(GameManager gameManager) {
        Terrain terrain = gameManager.getTerrain();
        System.out.println("\n🗺️  Carte (ligne d'eau Y=" + Terrain.getWaterLevel() + "):");

        int hauteur = Math.min(terrain.getCollisionsHeight(), 25);
        int largeur = Math.min(terrain.getCollisionsWidth(), 60);

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                System.out.print(symbolePourTuile(x, y, partie));
            }
            System.out.println();
        }
        System.out.println();
    }

    public char symbolePourTuile(int x, int y, Partie partie) {
        Worm w = partie.trouverWormAPosition(x, y);
        Terrain terrain = partie.getTerrain();
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

    public void afficherEtatApresChute(GameManager gameManager) {
        System.out.println("\n💥 ÉTAT DES WORMS APRÈS LA CHUTE :\n");

        for (int i = 0; i < 2; i++) {
            System.out.println("🔹 " + gameManager.getTeam(i).getName() + " :");

            Alien[] aliens = gameManager.getTeam(i).getWorms();
            for (int j = 0; j < aliens.length; j++) {
                Alien w = aliens[j];
                if (w != null && w.isAlive()) {
                    System.out.println("   → Worm #" + (j + 1) + " : PV = "
                            + w.getHealthPoints() + "/100");
                }
            }
        }
        System.out.println();
    }


    public void afficherResultatDeplacement(boolean succes, String direction) {
        if (succes) {
            System.out.println("✅ Déplacement à " + direction + " réussi");
        } else {
            System.out.println("❌ Déplacement impossible (collision ou hors limites)");
        }
    }

    public void afficherResultatSaut(boolean succes) {
        if (succes) {
            System.out.println("✅ Le worm a sauté !");
        } else {
            System.out.println("❌ Saut impossible");
        }
    }

    public void afficherResultatAttaque(Alien cible, int x, int y, Team teamActive) {
        if (cible == null) {
            System.out.println("❌ Aucun worm à la position (" + x + ", " + y + ")");
            return;
        }

        boolean estAllie = (cible.getTeam() == teamActive);
        boolean wormMort = !cible.isAlive();

        if (estAllie) {
            if (wormMort) {
                System.out.println("💀 TIR AMI FATAL ! Vous avez tué votre propre worm !");
            } else {
                System.out.println("⚠️  TIR AMI ! Votre worm a été touché (PV restants: "
                        + cible.getHealthPoints() + ")");

            }
        } else {
            if (wormMort) {
                System.out.println("💀 Worm ennemi éliminé !");
            } else {
                System.out.println("💥 Worm ennemi touché ! Il lui reste "
                        + cible.getHealthPoints() + " PV");
            }
        }
    }

    public void afficherTourPasse() {
        System.out.println("⏭️  Tour passé");
    }

    public void afficherPartiAbandon() {
        System.out.println("\n❌ Partie abandonnée.");
    }

    public void afficherCommandeInconnue() {
        System.out.println("❌ Commande inconnue ! Tapez 'info' pour l'aide.");
    }

    public void afficherMessage(String message) {
        System.out.println(message);
    }

    public String demanderCommande() {
        System.out.print("\n💬 > ");
        return scanner.nextLine().toLowerCase().trim();
    }

    public int demanderEntier(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("⚠️  Veuillez entrer un nombre valide: ");
        }
        int valeur = scanner.nextInt();
        scanner.nextLine();
        return valeur;
    }

    public void close() {
        scanner.close();
        System.out.println("\n👋 Merci d'avoir joué !");
    }
}
