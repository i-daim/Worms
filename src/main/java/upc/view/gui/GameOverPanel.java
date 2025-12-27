package upc.view.gui;

import upc.assets.ActiveAnimation;
import upc.assets.Animation;
import upc.assets.AnimationManager;
import upc.assets.SoundManager;
import upc.model.jeu.Team;


import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GameOverPanel extends JPanel {

    private static final Color BACKGROUND_COLOR = new Color(20, 20, 30);
    private static final Color TITLE_COLOR = new Color(255, 215, 0);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color STATS_COLOR = new Color(200, 200, 200);
    private static final Color QUIT_BUTTON_COLOR = new Color(180, 50, 50);

    private final Team winningTeam;
    private ActiveAnimation victoryAnimation;

    public GameOverPanel(Team winningTeam) {
        this.winningTeam = winningTeam;

        initializeUI();
        initializeAnimation();

        SoundManager.playMusic("Victory Music");
    }


////    GESTION DE L'INTERFACE    ////
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


////    GESTION DE L'ANIMATION    ////
    private void initializeAnimation() {
        String path = "victory_" + winningTeam.getName().toLowerCase();
        Animation anim = AnimationManager.getAnimation(path);
        victoryAnimation = new ActiveAnimation(anim, 70, true, 0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (victoryAnimation != null) {
            victoryAnimation.update();

            BufferedImage frame = victoryAnimation.getCurrentFrame();
            int frameWidth = frame.getWidth();
            int frameHeight = frame.getHeight();

            int xLeft = (int) (getWidth() * 0.1);
            int xRight = (int) (getWidth() * 0.9) - frameWidth;

            int y = (getHeight() - frameHeight) / 2;

            g.drawImage(frame, xLeft, y, null);
            g.drawImage(frame, xRight, y, null);

            repaint();
        }
    }


////    GESTION DES TITRES/BOUTONS    ////
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
        return panel;
    }

    private JLabel createTitleLabel() {
        JLabel label = new JLabel("VICTOIRE !");
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
