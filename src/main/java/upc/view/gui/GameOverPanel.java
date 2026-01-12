package upc.view.gui;

import upc.assets.ActiveAnimation;
import upc.assets.Animation;
import upc.assets.AnimationManager;
import upc.assets.SoundManager;
import upc.controller.MenuController;
import upc.model.jeu.Team;


import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Panneau d'affichage de fin de partie.
 * <p>
 * Cette classe représente l'écran affiché lorsqu'une équipe remporte la victoire.
 * Elle est responsable de :
 * <ul>
 * <li>Afficher le nom de l'équipe gagnante et ses statistiques.</li>
 * <li>Jouer une animation de victoire spécifique à l'équipe.</li>
 * <li>Jouer la musique de victoire.</li>
 * <li>Fournir les boutons de navigation.</li>
 * </ul>
 */
public class GameOverPanel extends JPanel {

    private static final Color BACKGROUND_COLOR = new Color(20, 20, 30);
    private static final Color TITLE_COLOR = new Color(255, 215, 0);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color STATS_COLOR = new Color(200, 200, 200);
    private static final Color QUIT_BUTTON_COLOR = new Color(180, 50, 50);

    private final Team winningTeam;
    private ActiveAnimation victoryAnimation;

    private final double zoom = GamePanel.ZOOM;

    /**
     * Construit l'écran de fin de partie.
     * <p>
     * Initialise l'interface graphique, charge les animations correspondant à l'équipe
     * gagnante et lance la musique de victoire.
     * </p>
     * @param winningTeam L'équipe qui a remporté la partie.
     */
    public GameOverPanel(Team winningTeam) {
        this.winningTeam = winningTeam;

        initializeUI();
        initializeAnimation();

        SoundManager.playMusic("Victory Music");
    }


////    GESTION DE L'INTERFACE    ////
    /**
     * Configure la disposition globale du panneau.
     * <p>
     * Utilise un BorderLayout pour placer le panneau central contenant
     * les textes et les boutons.
     * </p>
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        JPanel centerPanel = createCenterPanel();

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(createTitleLabel());
        centerPanel.add(Box.createVerticalStrut((int) (15 * zoom)));
        centerPanel.add(createWinnerLabel());
        centerPanel.add(Box.createVerticalStrut((int) (10 * zoom)));
        centerPanel.add(createStatsLabel());
        centerPanel.add(Box.createVerticalStrut((int) (30 * zoom)));
        centerPanel.add(createBackButton());
        centerPanel.add(Box.createVerticalStrut((int) (10 * zoom)));
        centerPanel.add(createQuitButton());
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);
    }


////    GESTION DE L'ANIMATION    ////
    /**
     * Initialise l'animation de victoire.
     * <p>
     * Récupère l'animation correspondante au nom de l'équipe via l'AnimationManager.
     * </p>
     */
    private void initializeAnimation() {
        String path = "victory_" + winningTeam.getName().toLowerCase();
        Animation anim = AnimationManager.getAnimation(path);
        victoryAnimation = new ActiveAnimation(anim, 70, true, 0, 0);
    }

    /**
     * Dessine le contenu du panneau, y compris les animations.
     * <p>
     * Cette méthode est appelée à chaque rafraîchissement. Elle met à jour l'état de
     * l'animation et dessine la frame courant de chaque côté de l'écran.
     * </p>
     * @param g L'objet Graphics utilisé pour dessiner.
     */
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
    /**
     * Crée le conteneur central pour les éléments textuels et les boutons.
     *
     * @return Un JPanel configuré avec un {@link BoxLayout} vertical et des marges.
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        int vMargin = (int) (60 * zoom);
        panel.setBorder(BorderFactory.createEmptyBorder(vMargin, 50, vMargin, 50));
        return panel;
    }

    /**
     * Crée le label du titre principal "VICTOIRE !".
     *
     * @return Le JLabel stylisé.
     */
    private JLabel createTitleLabel() {
        JLabel label = new JLabel("VICTOIRE !");
        label.setFont(new Font("Arial", Font.BOLD, (int) (45 * zoom)));
        label.setForeground(TITLE_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
     * Crée le label indiquant le nom de l'équipe gagnante.
     *
     * @return Le JLabel stylisé.
     */
    private JLabel createWinnerLabel() {
        JLabel label = new JLabel("Les " + winningTeam.getName() + " ont gagné !");
        label.setFont(new Font("Arial", Font.BOLD, (int) (30 * zoom)));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
     * Crée le label affichant les statistiques de fin de partie.
     * <p>
     * Affiche notamment le nombre d'aliens survivants de l'équipe gagnante.
     * </p>
     * @return Le JLabel stylisé.
     */
    private JLabel createStatsLabel() {
        JLabel label = new JLabel(winningTeam.getAliveAliensNumber() + " aliens survivants");
        label.setFont(new Font("Arial", Font.PLAIN, (int) (16 * zoom)));
        label.setForeground(STATS_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
     * Crée le bouton permettant de retourner au menu principal.
     * <p>
     * L'action associée remplace le contenu de la fenêtre principale
     * par un nouveau {@link MenuPanel}.
     * </p>
     * @return Le JButton configuré via {@link ButtonManager}.
     */
    private JButton createBackButton() {
        JButton button = ButtonManager.createGameButton("Back To Menu");

        button.addActionListener(e -> {
            MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
            if (mainFrame != null) {
                MenuPanel menuPanel = new MenuPanel();
                new MenuController(mainFrame, menuPanel);

                mainFrame.setContentPane(menuPanel);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });

        button.setBackground(QUIT_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        return button;
    }

    /**
     * Crée le bouton permettant de quitter l'application.
     *
     * @return Le JButton configuré pour fermer le programme.
     */
    private JButton createQuitButton() {
        JButton button = ButtonManager.createGameButton("Quit Game", e -> System.exit(0));
        button.setBackground(QUIT_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        return button;
    }
}