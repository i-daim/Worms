package upc.view.gui;

import upc.model.jeu.Team;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {

    public GameOverPanel(Team winningTeam) {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel titleLabel = new JLabel("🎉 VICTOIRE ! 🎉");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel winnerLabel = new JLabel(winningTeam.getName() + " a gagné !");
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 48));
        winnerLabel.setForeground(Color.WHITE);
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statsLabel = new JLabel(
                winningTeam.getAliveAliensNumber() + " worms survivants"
        );
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        statsLabel.setForeground(new Color(200, 200, 200));
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(winnerLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(statsLabel);
        centerPanel.add(Box.createVerticalStrut(50));

        JButton quitButton = new JButton("Quitter le jeu");
        quitButton.setFont(new Font("Arial", Font.BOLD, 24));
        quitButton.setPreferredSize(new Dimension(250, 60));
        quitButton.setMaximumSize(new Dimension(250, 60));
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setFocusPainted(false);
        quitButton.setBackground(new Color(180, 50, 50));
        quitButton.setForeground(Color.WHITE);
        quitButton.addActionListener(e -> System.exit(0));

        centerPanel.add(quitButton);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);
    }
}
