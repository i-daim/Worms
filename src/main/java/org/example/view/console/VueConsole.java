package org.example.view.console;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Partie;
import org.example.model.jeu.Terrain;

import java.util.Scanner;

public class VueConsole {
    private final Scanner scanner;

    public VueConsole() {
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

    public void afficherEtatPartie(Partie partie) {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("📊 " + partie.toString());
        System.out.println("─".repeat(60));

        afficherStatistiquesEquipes(partie);
        afficherWormActif(partie);

        System.out.println("─".repeat(60));
    }

    private void afficherStatistiquesEquipes(Partie partie) {
        System.out.println("\n🏆 État des équipes:");

        Equipe[] equipes = partie.getEquipes();
        for (int i = 0; i < equipes.length; i++) {
            String indicateur = (i == partie.getIndexEquipeActive()) ? "►" : " ";
            System.out.println(indicateur + "  " + equipes[i].toString());
        }
    }

    private void afficherWormActif(Partie partie) {
        Worm worm = partie.getWormActif();

        if (worm != null) {
            Position pos = worm.getPosition();
            System.out.println("\n🐛 Worm actif (" + partie.getEquipeActive().getNom() + "):");
            System.out.printf("   Position: %s\n", pos);
            System.out.printf("   PV: %d/100 ", worm.getPointsDeVie());
            afficherBarreVie(worm.getPointsDeVie());
        } else {
            System.out.println("\n⚠️  Aucun worm actif");
        }
    }

    private void afficherWormsEquipe(Equipe equipe, Worm wormActif) {
        Worm[] worms = equipe.getWorms();
        int count = 1;
        boolean aucunVivant = true;

        for (Worm worm : worms) {
            if (worm != null && worm.estVivant()) {
                aucunVivant = false;
                Position pos = worm.getPosition();
                String indicateur = (worm == wormActif) ? "► " : "  ";

                System.out.printf("%sWorm #%d | Position: %s | PV: %3d/100 ",
                        indicateur, count, pos, worm.getPointsDeVie());
                afficherBarreVie(worm.getPointsDeVie());
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

    public void afficherFinPartie(Partie partie) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("            🎉 PARTIE TERMINÉE ! 🎉");
        System.out.println("=".repeat(60));

        Equipe gagnante = partie.getEquipeGagnante();
        if (gagnante != null) {
            System.out.println("\n🏆 " + gagnante.getNom() + " a gagné ! 🏆\n");
        }

        afficherStatistiquesEquipes(partie);
        System.out.println("\n" + "=".repeat(60));
    }

    public void afficherTerrain(Terrain terrain) {
        System.out.println("\n🗺️  Carte (ligne d'eau Y=" + Terrain.getLigneEau() + "):");

        int hauteur = Math.min(terrain.getHauteur(), 25);
        int largeur = Math.min(terrain.getLargeur(), 60);

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int tileId = terrain.getTileId(x, y);
                System.out.print(getCharForTile(tileId));
            }
            System.out.println();
        }
        System.out.println();
    }

    private char getCharForTile(int tileId) {
        switch (tileId) {
            case 0: return ' ';
            case 1: return '▓';
            case 2: return '█';
            case 3: return '■';
            default: return '?';
        }
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

    public void afficherResultatDegats(boolean succes, int degats) {
        if (succes) {
            System.out.println("💥 " + degats + " points de dégâts infligés !");
        } else {
            System.out.println("❌ Aucune cible ennemie trouvée");
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
