package org.example.view.gui;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.Font;

public class MenuPanel extends JPanel {

    private Image background;
    private final JButton boutonNewGame = new JButton("Nouvelle Partie");
    private final JButton boutonLoadGame = new JButton("Charger Partie");
    private final JButton boutonQuitter = new JButton("Quitter");


    public MenuPanel() {
        // Chargement de l'image d'arrière plan
        try{
            background = new ImageIcon(getClass().getResource("/images/menu/background.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de l'image");
        }

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        boutonNewGame.setAlignmentX(CENTER_ALIGNMENT);
        boutonLoadGame.setAlignmentX(CENTER_ALIGNMENT);
        boutonQuitter.setAlignmentX(CENTER_ALIGNMENT);

        buttonPanel.add(boutonNewGame);
        buttonPanel.add(boutonLoadGame);
        buttonPanel.add(boutonQuitter);

        this.add(Box.createVerticalGlue());
        this.add(buttonPanel);
        this.add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
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