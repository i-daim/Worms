package upc.view.gui;

import upc.assets.ImageManager;
import upc.assets.SoundManager;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.Dimension;

/**
 * Panneau de configuration et de réglages du jeu.
 * <p>
 * Ce panneau permet à l'utilisateur de modifier les préférences globales, telles que :
 * <ul>
 * <li>La musique de fond (via liste déroulante).</li>
 * <li>Le volume sonore global (via curseur).</li>
 * </ul>
 * Il contient également un bouton de retour pour revenir au menu précédent.
 */
public class SettingsPanel extends JPanel {

    private final Image background = ImageManager.get("settings_background");

    private final JButton backButton;
    private JComboBox<String> musicSelection;
    private JSlider volumeSlider;

    private final double zoom = GamePanel.ZOOM;

    /**
     * Constructeur du panneau de réglages.
     * <p>
     * Initialise la mise en page via BoxLayout verticale "Settings"
     * et le bouton de retour. Appelle ensuite {@link #initControls()} pour placer
     * les composants interactifs.
     * </p>
     */
    public SettingsPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Arial", Font.BOLD, (int) (14 * zoom)));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setAlignmentY(Component.TOP_ALIGNMENT);
        title.setForeground(Color.WHITE);

        backButton = ButtonManager.createGameButton("Back");
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.setAlignmentY(Component.TOP_ALIGNMENT);

        this.add(Box.createHorizontalStrut(30));
        this.add(title);
        this.add(Box.createVerticalStrut((int) (16 * zoom)));

        initControls();

        this.add(Box.createVerticalStrut((int) (16 * zoom)));
        this.add(backButton);
        this.add(Box.createVerticalGlue());
        this.add(Box.createVerticalGlue());
    }

    /**
     * Initialise les composants de contrôle audio et leurs labels.
     * <p>
     * Crée et configure graphiquement :
     * <ul>
     * <li><b>Sélecteur de musique</b> : Une {@link JComboBox} remplie avec
     * les musiques disponibles dans le {@link SoundManager}.</li>
     * <li><b>Contrôle du volume</b> : Un {@link JSlider} allant de 0 à 100 (défaut 50).</li>
     * </ul>
     */
    private void initControls() {
        JLabel musicLabel = new JLabel("Music:");
        musicLabel.setFont(new Font("Arial", Font.BOLD, (int) (10 * zoom)));
        musicLabel.setForeground(Color.WHITE);
        musicLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] musics = SoundManager.getMusicsSelection();
        musicSelection = new JComboBox<>(musics);
        musicSelection.setSelectedIndex(0);
        musicSelection.setAlignmentX(Component.LEFT_ALIGNMENT);
        musicSelection.setFont(new Font("Arial", Font.BOLD, (int) (9 * zoom)));
        musicSelection.setForeground(new Color(126, 217, 87));
        musicSelection.setBackground(new Color(55, 65, 75));
        musicSelection.setSelectedIndex(1);

        Dimension d = new Dimension((int) (140 * zoom), (int) (20 * zoom));
        musicSelection.setMaximumSize(d);
        musicSelection.setPreferredSize(d);

        JLabel volumeLabel = new JLabel("Volume:");
        volumeLabel.setFont(new Font("Arial", Font.BOLD, (int) (10 * zoom)));
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        volumeSlider.setOpaque(false);
        volumeSlider.setMaximumSize(new Dimension((int) (150 * zoom), (int) (25 * zoom)));
        volumeSlider.setPreferredSize(new Dimension((int) (150 * zoom), (int) (25 * zoom)));

        this.add(musicLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(musicSelection);
        this.add(Box.createVerticalStrut((int) (15 * zoom)));
        this.add(volumeLabel);
        this.add(Box.createVerticalStrut(5));
        this.add(volumeSlider);
    }

    /**
     * Dessine l'arrière-plan du panneau de paramètres.
     * <p>
     * Utilise "settings_background" chargée via {@link ImageManager}.
     * </p>
     * @param g L'objet Graphics.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Récupère le bouton de retour.
     * @return Le JButton "Back".
     */
    public JButton getBackButton() {
        return backButton;
    }

    /**
     * Récupère la liste déroulante de sélection de musique.
     * @return La JComboBox contenant les titres de musique.
     */
    public JComboBox<String> getMusicSelection() {
        return musicSelection;
    }

    /**
     * Récupère le curseur de volume.
     * @return Le JSlider de volume.
     */
    public JSlider getVolumeSlider() {
        return volumeSlider;
    }
}