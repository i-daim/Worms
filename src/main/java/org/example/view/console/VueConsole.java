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

    public void afficherEtat(Worm worm) {
        Position pos = worm.getPosition();
        System.out.println("\n--- État du Worm ---");
        System.out.printf("Position: (%d, %d)\n", pos.getX(), pos.getY());
        System.out.printf("Points de vie: %d\n", worm.getPointsDeVie());
        System.out.printf("Vivant: %s\n", worm.estVivant() ? "Oui" : "Non");
        System.out.println("--------------------");
    }

    public String demanderCommande() {
        System.out.print("> ");
        return scanner.next().toLowerCase();
    }

    public int demanderEntier() {
        System.out.print("Combien de dégâts? ");
        int nombre = scanner.nextInt();
        return nombre;
    }

    public void afficherMessage(String message) {
        System.out.println(message);
    }

    public void close() {
        scanner.close();
        System.out.println("\nMerci d'avoir joué!");
    }
}
