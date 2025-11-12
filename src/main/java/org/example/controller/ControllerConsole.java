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
        vue.afficherEtatPartie(partie);

        while (enCours && !partie.estTerminee()) {
            traiterCommande();
        }

        if (partie.estTerminee()) {
            vue.afficherFinPartie(partie);
        }

        vue.close();
    }
}
