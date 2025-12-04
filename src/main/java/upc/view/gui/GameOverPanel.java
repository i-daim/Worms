package upc.view.gui;
import upc.model.jeu.Team;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {

    public GameOverPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        add(centerPanel, BorderLayout.CENTER);

        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel titleLabel = new JLabel("🎉 VICTOIRE ! 🎉");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(titleLabel);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setForeground(new Color(255, 215, 0));

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);

    }
    public GameOverPanel(Team winningTeam) {
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(30));
        JLabel winnerLabel = new JLabel(winningTeam.getName() + " a gagné !");
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(winnerLabel);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 48));
        winnerLabel.setForeground(Color.WHITE);

        centerPanel.add(winnerLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        JLabel statsLabel = new JLabel(
                winningTeam.getAliveAliensNumber() + " worms survivants"
        );
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(statsLabel);

        statsLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        statsLabel.setForeground(new Color(200, 200, 200));

        centerPanel.add(statsLabel);
        centerPanel.add(Box.createVerticalStrut(50));

        JButton quitButton = new JButton("Quitter le jeu");
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(quitButton);








    }


}
