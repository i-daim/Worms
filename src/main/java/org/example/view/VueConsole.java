package org.example.view;

import org.example.model.entites.Position;
import org.example.model.entites.Worm;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Monde;

import java.util.Scanner;

public class VueConsole {
    public static void main(String[] args) {
        Equipe equipe = new Equipe("Team A", 1);
        Monde monde = new Monde(10, 10);
        Worm worm = new Worm(100, equipe, 5, 5);

        Scanner scanner = new Scanner(System.in);
        String commande = "";

        System.out.println("Bienvenue dans la vue console de Worms !");
        System.out.println(
                "Commandes : gauche | droite | sauter | degats [n] | exit");

        while (!commande.equals("exit")) {
            afficherEtat(worm);

            System.out.print("> ");
            commande = scanner.next();

            switch (commande) {
                case "gauche":
                    if (worm.deplacerGauche(monde)) {
                        System.out.println(
                                "Déplacement vers la gauche réussi.");
                    } else {
                        System.out.println(
                                "Impossible de se déplacer vers la gauche.");
                    }
                    break;

                case "droite":
                    if (worm.deplacerDroite(monde)) {
                        System.out.println(
                                "Déplacement vers la droite réussi.");
                    } else {
                        System.out.println(
                                "Impossible de se déplacer vers la droite.");
                    }
                    break;

                case "sauter":
                    if (worm.sauter(monde)) {
                        System.out.println("Saut réussi.");
                    } else {
                        System.out.println("Impossible de sauter.");
                    }
                    break;

                case "degats":
                    int pts = scanner.nextInt();
                    worm.subirDegats(pts);
                    System.out.println(
                            "Le ver a subi " + pts + " points de dégâts.");
                    break;

                case "exit":
                    System.out.println("Sortie.");
                    break;

                default:
                    System.out.println("Commande inconnue !");
            }

            if (!worm.estVivant()) {
                System.out.println("Le ver est mort. Fin de la partie.");
                break;
            }
        }
        scanner.close();
    }

    public static void afficherEtat(Worm worm) {
        Position pos = worm.getPosition();
        System.out.printf("Worm : Position=(%d,%d) | PV=%d | Vivant=%s\n",
            pos.getX(), pos.getY(), worm.getPointsDeVie(), worm.estVivant());
    }
}
