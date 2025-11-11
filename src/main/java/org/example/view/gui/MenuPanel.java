package org.example.view.gui;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;

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

    public void addNewGameListener(ActionListener listener) {
        boutonNewGame.addActionListener(listener);
    }

    public void addLoadGameListener(ActionListener listener) {
        boutonLoadGame.addActionListener(listener);
    }

    public void addQuitterListener(ActionListener listener) {
        boutonQuitter.addActionListener(listener);
    }
}