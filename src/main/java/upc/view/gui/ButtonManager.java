package upc.view.gui;

import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

/**
 * Classe utilitaire pour la création et la personnalisation des boutons de l'interface graphique.
 * <p>
 * Cette classe agit comme une fabrique de boutons Swing, garantissant une cohérence visuelle
 * dans toute l'application. Elle permet de créer des boutons avec :
 * <ul>
 * <li>Un rendu graphique personnalisé.</li>
 * <li>Des couleurs réactives aux états (survol, clic, etc).</li>
 * <li>Une mise à l'échelle automatique basé sur {@link GamePanel#ZOOM}.</li>
 * </ul>
 */
public class ButtonManager {

    /**
     * Crée un bouton entièrement stylisé avec un rendu graphique personnalisé.
     * <p>
     * Cette méthode surcharge la méthode {@code paintComponent} du bouton pour dessiner
     * manuellement le fond et les bordures avec des formes arrondies.
     * </p>
     * <p>
     * Les états gérés visuellement sont :
     * <ul>
     * <li><b>Normal</b> : Gris foncé (55, 65, 75).</li>
     * <li><b>Survol </b> : Gris plus clair (75, 85, 95).</li>
     * <li><b>Appuyé </b> : Gris très sombre (35, 45, 55).</li>
     * </ul>
     *
     * @param text Le texte à afficher sur le bouton.
     * @return Une instance de {@link JButton} configurée avec les dimensions et le style du jeu.
     */
    public static JButton createGameButton(String text) {
        double zoom = GamePanel.ZOOM;

        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Change la couleur quand le bouton est survolé/appuyé
                if (getModel().isPressed()) {
                    g2.setColor(new Color(35, 45, 55));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(75, 85, 95));
                } else {
                    g2.setColor(new Color(55, 65, 75));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Bordure du bouton
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

                super.paintComponent(g);
            }
        };

        // Style du texte affiché
        btn.setForeground(new Color(126, 217, 87));
        btn.setFont(new Font("Arial", Font.BOLD, (int) (10 * zoom)));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Dimensions du bouton
        Dimension size = new Dimension((int) (111 * zoom), (int) (27 * zoom));
        btn.setMaximumSize(size); // pour avoir les mm dimensions
        btn.setPreferredSize(size);

        return btn;
    }

    /**
     * Crée un bouton stylisé et lui associe directement une action.
     * <p>
     * C'est une méthode de commodité qui appelle {@link #createGameButton(String)}
     * puis ajoute l'{@link ActionListener} fourni.
     * </p>
     *
     * @param text Le texte du bouton.
     * @param listener L'écouteur d'événement à déclencher lors du clic.
     * @return Le bouton configuré et prêt à l'emploi.
     */
    public static JButton createGameButton(String text, ActionListener listener) {
        JButton btn = createGameButton(text);
        btn.addActionListener(listener);
        return btn;
    }
}