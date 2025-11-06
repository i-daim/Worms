package org.example.view;

import org.example.model.entites.Worm;
import org.example.model.entites.Position;
import org.example.model.jeu.Equipe;
import org.example.model.jeu.Monde;

import java.util.Scanner;

public class VueConsole {
    public static void main(String[] args) {
        Equipe equipe = new Equipe("Team A", 1);
        Monde monde = new Monde(100, 100);
        Worm worm = new Worm(100, equipe, 50, 50);  // Position : (50, 50) au lieu de (50, 5)


        Scanner scanner = new Scanner(System.in);
        String commande = "";

        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║   Bienvenue dans la vue console Worms !   ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.println("Commandes : gauche | droite | sauter | degats [n] | demo | exit\n");

        while (!commande.equals("exit")) {
            afficherEtat(worm);

            System.out.print("> ");
            commande = scanner.next();

            switch (commande) {
                case "gauche":
                    if (worm.estVivant()) {
                        if (worm.deplacerGauche(monde)) {
                            System.out.println("✓ Déplacement vers la gauche réussi.");
                        } else {
                            System.out.println("✗ Impossible de se déplacer vers la gauche (hors limites).");
                        }
                    } else {
                        System.out.println("✗ Le ver est mort, impossible de bouger !");
                    }
                    break;

                case "droite":
                    if (worm.estVivant()) {
                        if (worm.deplacerDroite(monde)) {
                            System.out.println("✓ Déplacement vers la droite réussi.");
                        } else {
                            System.out.println("✗ Impossible de se déplacer vers la droite (hors limites).");
                        }
                    } else {
                        System.out.println("✗ Le ver est mort, impossible de bouger !");
                    }
                    break;

                case "sauter":
                    if (worm.estVivant()) {
                        if (worm.sauter(monde)) {
                            System.out.println("✓ Saut réussi.");
                        } else {
                            System.out.println("✗ Impossible de sauter (hors limites).");
                        }
                    } else {
                        System.out.println("✗ Le ver est mort, impossible de bouger !");
                    }
                    break;

                case "degats":
                    if (worm.estVivant()) {
                        int pts = scanner.nextInt();
                        int pvAvant = worm.getPointsDeVie();
                        worm.subirDegats(pts);
                        int pvApres = worm.getPointsDeVie();
                        System.out.println("➜ Le ver a subi " + pts + " points de dégâts.");
                        System.out.println("  PV avant : " + pvAvant + " → PV après : " + pvApres);
                    } else {
                        System.out.println("✗ Le ver est déjà mort !");
                    }
                    break;

                case "demo":
                    demonstrationComplete(equipe, monde);
                    System.out.println("Retour à la partie interactive...\n");
                    worm = new Worm(100, equipe, 5, 5); // Réinitialiser le ver
                    break;

                case "exit":
                    System.out.println("\n╔═══════════════════════════════════════════╗");
                    System.out.println("║   Au revoir !                             ║");
                    System.out.println("╚═══════════════════════════════════════════╝\n");
                    break;

                default:
                    System.out.println("✗ Commande inconnue !");
            }

            if (!worm.estVivant()) {
                System.out.println("\n╔═══════════════════════════════════════════╗");
                System.out.println("║   Le ver est mort. Fin de la partie.      ║");
                System.out.println("╚═══════════════════════════════════════════╝\n");
                break;
            }

            System.out.println();
        }
        scanner.close();
    }

    public static void afficherEtat(Worm worm) {
        Position pos = worm.getPosition();
        String statut = worm.estVivant() ? "✓ Vivant" : "✗ Mort";
        System.out.printf("║ Position: (%d, %d) │ PV: %3d/100 │ Statut: %s ║\n",
                pos.x, pos.y, worm.getPointsDeVie(), statut);
    }

    public static void demonstrationComplete(Equipe equipe, Monde monde) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                DÉMONSTRATION COMPLÈTE                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");

        Worm worm = new Worm(100, equipe, 5, 5);

        // Test 1 : État initial
        System.out.println("► TEST 1 : État initial du ver");
        afficherEtat(worm);
        System.out.println();

        // Test 2 : Déplacement droite
        System.out.println("► TEST 2 : Déplacement à droite (3 fois)");
        for (int i = 0; i < 3; i++) {
            worm.deplacerDroite(monde);
            afficherEtat(worm);
        }
        System.out.println();

        // Test 3 : Déplacement gauche
        System.out.println("► TEST 3 : Déplacement à gauche (2 fois)");
        for (int i = 0; i < 2; i++) {
            worm.deplacerGauche(monde);
            afficherEtat(worm);
        }
        System.out.println();

        // Test 4 : Saut
        System.out.println("► TEST 4 : Saut vers le haut (2 fois)");
        for (int i = 0; i < 2; i++) {
            worm.sauter(monde);
            afficherEtat(worm);
        }
        System.out.println();

        // Test 5 : Dégâts progressifs
        System.out.println("► TEST 5 : Dégâts progressifs");
        int[] damageList = {10, 20, 15};
        for (int dmg : damageList) {
            System.out.println("   Attaque de " + dmg + " points...");
            worm.subirDegats(dmg);
            afficherEtat(worm);
        }
        System.out.println();

        // Test 6 : Ver mort
        System.out.println("► TEST 6 : Coup létal (60 points)");
        System.out.println("   Attaque de 60 points...");
        worm.subirDegats(60);
        afficherEtat(worm);
        System.out.println();

        // Test 7 : Immuabilité
        System.out.println("► TEST 7 : Immuabilité de Position");
        Position p1 = new Position(5, 5);
        Position p2 = p1.deplacer(2, 1);
        System.out.printf("   p1 = (5, 5), p2 = p1.deplacer(2, 1) = (%d, %d)\n", p2.x, p2.y);
        System.out.printf("   p1 reste inchangée : (%d, %d) ✓\n", p1.x, p1.y);
        System.out.println();

        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              ✓ DÉMONSTRATION TERMINÉE                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
    }
}
