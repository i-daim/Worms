package org.example.view.gui;

import javax.swing.JPanel;
import javax.swing.JButton;

public class MenuPanel extends JPanel {

    private final JButton boutonNewGame;
    private final JButton boutonLoadGame;
    private final JButton boutonQuitter;

    public MenuPanel() {
        boutonNewGame = new JButton("Nouvelle Partie");
        boutonLoadGame = new JButton("Charger Partie");
        boutonQuitter = new JButton("Quitter");

        this.add(boutonNewGame);
        this.add(boutonLoadGame);
        this.add(boutonQuitter);
    }

    public JButton getBoutonNewGame() {
        return boutonNewGame;
    }
    public JButton getBoutonLoadGame() {
        return boutonLoadGame;
    }
    public JButton getBoutonQuitter() {
        return boutonQuitter;
    }

}