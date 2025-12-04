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


}
