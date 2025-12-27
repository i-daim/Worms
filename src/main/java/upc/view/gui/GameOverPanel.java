package upc.view.gui;

import upc.model.jeu.Team;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;


public class GameOverPanel extends JPanel {

    private static final Color BACKGROUND_COLOR = new Color(20, 20, 30);
    private static final Color TITLE_COLOR = new Color(255, 215, 0);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color STATS_COLOR = new Color(200, 200, 200);
    private static final Color QUIT_BUTTON_COLOR = new Color(180, 50, 50);

    private final Team winningTeam;

    public GameOverPanel(Team winningTeam) {
        this.winningTeam = winningTeam;

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        JPanel centerPanel = createCenterPanel();

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(createTitleLabel());
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(createWinnerLabel());
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(createStatsLabel());
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(createQuitButton());
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
        return panel;
    }

    private JLabel createTitleLabel() {
        JLabel label = new JLabel("🎉 VICTOIRE ! 🎉");
        label.setFont(new Font("Arial", Font.BOLD, 72));
        label.setForeground(TITLE_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createWinnerLabel() {
        JLabel label = new JLabel("Les " + winningTeam.getName() + " ont gagné !");
        label.setFont(new Font("Arial", Font.BOLD, 48));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createStatsLabel() {
        JLabel label = new JLabel(winningTeam.getAliveAliensNumber() + " aliens survivants");
        label.setFont(new Font("Arial", Font.PLAIN, 24));
        label.setForeground(STATS_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JButton createQuitButton() {
        JButton button = ButtonManager.createGameButton("Quitter le jeu", e -> System.exit(0));
        button.setBackground(QUIT_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        return button;
    }
}
