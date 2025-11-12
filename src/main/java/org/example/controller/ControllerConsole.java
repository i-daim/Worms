package org.example.controller;

import org.example.model.jeu.Partie;
import org.example.view.console.VueConsole;


public class ControllerConsole {
    private final Partie partie;
    private final VueConsole vue
    private boolean enCours;

    public ControllerConsole(int largeur, int hauteur) {
        // Créer le modèle (Partie contient tout)
        this.partie = new Partie(largeur, hauteur);

        // Créer la vue
        this.vue = new VueConsole();

        this.enCours = true;
    }

    public void demarrer() {
        vue.afficherBienvenue();
        String commande = "";

        while (!commande.equals("exit")) {
            vue.afficherEtat(worm);
            commande = vue.demanderCommande();

            switch (commande) {
                case "gauche":
                    if (worm.deplacerGauche(terrain)) {
                        vue.afficherMessage("✓ Déplacement vers la gauche réussi.");
                    } else {
                        vue.afficherMessage("✗ Impossible de se déplacer vers la gauche.");
                    }
                    break;

                case "droite":
                    if (worm.deplacerDroite(terrain)) {
                        vue.afficherMessage("✓ Déplacement vers la droite réussi.");
                    } else {
                        vue.afficherMessage("✗ Impossible de se déplacer vers la droite.");
                    }
                    break;

                case "sauter":
                    if (worm.sauter(terrain)) {
                        vue.afficherMessage("✓ Saut réussi.");
                    } else {
                        vue.afficherMessage("✗ Impossible de sauter.");
                    }
                    break;

                case "degats":
                    int pts = vue.demanderEntier();
                    worm.subirDegats(pts);
                    vue.afficherMessage("✓ Le worm a subi " + pts + " points de dégâts.");
                    break;

                case "exit":
                    vue.afficherMessage("Au revoir!");
                    break;

                default:
                    vue.afficherMessage("✗ Commande inconnue!");
            }

            if (!worm.estVivant()) {
                vue.afficherMessage("\n💀 Le worm est mort. Fin de la partie.");
                break;
            }
        }

        vue.close();
    }
}
