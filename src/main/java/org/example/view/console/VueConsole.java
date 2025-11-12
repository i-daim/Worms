package org.example.view.console;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Partie;

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

}
