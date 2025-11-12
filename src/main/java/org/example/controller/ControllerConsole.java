package org.example.controller;

import org.example.model.jeu.Partie;
import org.example.view.console.VueConsole;


public class ControllerConsole {
    private final Partie partie;
    private final VueConsole vue;
    private boolean enCours;

    public ControllerConsole(int largeur, int hauteur) {
        this.partie = new Partie(largeur, hauteur);

        // Créer la vue
        this.vue = new VueConsole();

        this.enCours = true;
    }

    public void demarrer() {
        vue.afficherBienvenue();
        vue.afficherEtatPartie(partie);

        while (enCours && !partie.estTerminee()) {
            traiterCommande();
        }

        if (partie.estTerminee()) {
            vue.afficherFinPartie(partie);
        }

        vue.close();
    }
    private void traiterCommande() {
        String commande = vue.demanderCommande();

        switch (commande) {
            case "gauche":
                boolean succesGauche = partie.deplacerWormActuelGauche();
                if (succesGauche) {
                    vue.afficherMessage("✅ Déplacement à gauche réussi");
                } else {
                    vue.afficherMessage("❌ Déplacement impossible (collision ou hors limites)");
                }
                vue.afficherEtatPartie(partie);
                break;

            case "droite":
                boolean succesDroite = partie.deplacerWormActuelDroite();
                if (succesDroite) {
                    vue.afficherMessage("✅ Déplacement à droite réussi");
                } else {
                    vue.afficherMessage("❌ Déplacement impossible (collision ou hors limites)");
                }
                vue.afficherEtatPartie(partie);
                break;

            case "sauter":
                boolean succesSaut = partie.sauterWormActuel();
                if (succesSaut) {
                    vue.afficherMessage("✅ Le worm a sauté !");
                } else {
                    vue.afficherMessage("❌ Saut impossible");
                }
                vue.afficherEtatPartie(partie);
                break;

            case "degats":
                int degats = vue.demanderEntier("💥 Combien de dégâts ? ");
                boolean degatsInfliges = partie.infligerDegatsEquipeEnnemie(degats);
                if (degatsInfliges) {
                    vue.afficherMessage("💥 " + degats + " points de dégâts infligés !");
                } else {
                    vue.afficherMessage("❌ Aucune cible ennemie trouvée");
                }
                vue.afficherEtatPartie(partie);
                break;

            case "passer":
                partie.passerTour();
                vue.afficherMessage("⏭️  Tour passé");
                vue.afficherEtatPartie(partie);
                break;

            case "info":
                vue.afficherEtatPartie(partie);
                break;

            case "exit":
                enCours = false;
                vue.afficherMessage("\n❌ Partie abandonnée.");
                break;

            default:
                vue.afficherMessage("❌ Commande inconnue ! Tapez 'info' pour l'aide.");
        }
    }
}
