package org.example.view.console;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;

import java.util.Scanner;

public class VueConsole {

    private final Scanner scanner;

    public VueConsole() {
        this.scanner = new Scanner(System.in);
    }

    public void afficherBienvenue() {
        System.out.println("Bienvenue dans la vue console de Worms !");
        System.out.println(
                "Commandes : gauche | droite | sauter | degats [n] | exit");
    }

    public String demanderCommande() {
        System.out.print("> ");
        return scanner.next();
    }

    public int demanderEntier() {
        return scanner.nextInt();
    }

    public void afficherEtat(Worm worm) {
        Position pos = worm.getPosition();
        System.out.printf("Worm : Position=(%d,%d) | PV=%d | Vivant=%s\n",
                pos.getX(), pos.getY(), worm.getPointsDeVie(), worm.estVivant());
    }

    public void afficherMessage(String message) {
        System.out.println(message);
    }

    public void close() {
        scanner.close();
    }
}
