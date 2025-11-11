package org.example.view.gui;

import javax.swing.JPanel;
import java.awt.Button;

public class MainPanel extends JPanel {

    private final Button boutonNewGame;
    private final Button boutonLoadGame;
    private final Button boutonQuitter;

    public MainPanel() {
        boutonNewGame = new Button("Nouvelle Partie");
        boutonLoadGame = new Button("Charger Partie");
        boutonQuitter = new Button("Quitter");

        this.add(boutonNewGame);
        this.add(boutonLoadGame);
        this.add(boutonQuitter);
    }

    public Button getBoutonNewGame() {
        return boutonNewGame;
    }
    public Button getBoutonLoadGame() {
        return boutonLoadGame;
    }
    public Button getBoutonQuitter() {
        return boutonQuitter;
    }

}