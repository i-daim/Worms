package org.example.view.gui;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {

    private final JButton boutonNewGame = new JButton("Nouvelle Partie");
    private final JButton boutonLoadGame = new JButton("Charger Partie");
    private final JButton boutonQuitter = new JButton("Quitter");

    public MenuPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Titre affiché dans le menu
        JLabel titre = new JLabel("Worms 0.5");
        titre.setFont(new Font("Impact", Font.BOLD, 48));
        titre.setAlignmentX(CENTER_ALIGNMENT);

        // Panel séparé pour les boutons, afin qu'ils soient pas "collés" au titre
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        boutonNewGame.setAlignmentX(CENTER_ALIGNMENT);
        boutonLoadGame.setAlignmentX(CENTER_ALIGNMENT);
        boutonQuitter.setAlignmentX(CENTER_ALIGNMENT);

        buttonPanel.add(boutonNewGame);
        buttonPanel.add(boutonLoadGame);
        buttonPanel.add(boutonQuitter);

        this.add(titre);
        this.add(Box.createVerticalGlue());
        this.add(buttonPanel);
        this.add(Box.createVerticalGlue());
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