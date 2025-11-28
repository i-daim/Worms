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
    private final JButton boutonNewGame = new JButton("New Game");
    private final JButton boutonLoadGame = new JButton("Load Game");
    private final JButton boutonQuitter = new JButton("Quit");


    public MenuPanel() {
        // Chargement de l'image d'arrière plan
        try {
            background = new ImageIcon(
                    getClass().getResource("/images/menu/background.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de l'image");
        }

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        boutonNewGame.setAlignmentX(CENTER_ALIGNMENT);
        boutonLoadGame.setAlignmentX(CENTER_ALIGNMENT);
        boutonQuitter.setAlignmentX(CENTER_ALIGNMENT);

        // Rend les boutons plus grands
        Dimension buttonSize = new Dimension(250, 50);
        Font buttonFont = new Font("Arial", Font.BOLD, 18);

        boutonNewGame.setPreferredSize(buttonSize);
        boutonNewGame.setFont(buttonFont);
        boutonLoadGame.setPreferredSize(buttonSize);
        boutonLoadGame.setFont(buttonFont);
        boutonQuitter.setPreferredSize(buttonSize);
        boutonQuitter.setFont(buttonFont);

        buttonPanel.add(boutonNewGame);
        buttonPanel.add(Box.createVerticalStrut(10)); // Ajout un espace entre les boutons
        buttonPanel.add(boutonLoadGame);
        buttonPanel.add(Box.createVerticalStrut(10));
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