package org.example.controller;

import org.example.model.entites.Worm;
import org.example.model.jeu.Monde;
import org.example.view.console.VueConsole;


public class ControllerConsole {
    private final Monde monde;
    private final Worm worm;
    private final VueConsole vue;

    public ControllerConsole(Monde monde, Worm worm, VueConsole vue) {
        this.monde = monde;
        this.worm = worm;
        this.vue = vue;
    }

    public void demarrer() {
        vue.afficherBienvenue();
        String commande = "";

        while (!commande.equals("exit")) {
            vue.afficherEtat(worm);
            commande = vue.demanderCommande();

            switch (commande) {
                case "gauche":
                    if (worm.deplacerGauche(monde)) {
                        vue.afficherMessage("Déplacement vers la gauche réussi.");
                    } else {
                        vue.afficherMessage("Impossible de se déplacer vers la gauche.");
                    }
                    break;

                case "droite":
                    if (worm.deplacerDroite(monde)) {
                        vue.afficherMessage("Déplacement vers la droite réussi.");
                    } else {
                        vue.afficherMessage("Impossible de se déplacer vers la droite.");
                    }
                    break;

                case "sauter":
                    if (worm.sauter(monde)) {
                        vue.afficherMessage("Saut réussi.");
                    } else {
                        vue.afficherMessage("Impossible de sauter.");
                    }
                    break;

                case "degats":
                    int pts = vue.demanderEntier();
                    worm.subirDegats(pts);
                    vue.afficherMessage("Le ver a subi " + pts + " points de dégâts.");
                    break;

                case "exit":
                    vue.afficherMessage("Sortie.");
                    break;

                default:
                    vue.afficherMessage("Commande inconnue !");
            }

            if (!worm.estVivant()) {
                vue.afficherMessage("Le ver est mort. Fin de la partie.");
                break;
            }
        }

        vue.close();
    }
}