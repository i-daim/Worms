package upc.view.gui;

import upc.assets.ImageManager;
import upc.assets.SoundManager;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

/**
 * Panneau représentant le menu principal du jeu.
 * <p>
 * Ce panneau sert de point d'entrée pour l'utilisateur et permet de :
 * <ul>
 * <li>Lancer une nouvelle partie.</li>
 * <li>Charger une partie existante.</li>
 * <li>Accéder aux paramètres.</li>
 * <li>Quitter l'application.</li>
 * </ul>
 * Il gère également l'affichage de l'arrière-plan du menu et la lecture de la musique d'ambiance.
 */
public class MenuPanel extends JPanel {

    private final Image background = ImageManager.get("menu_background");

    private final JButton newGame = ButtonManager.createGameButton("New Game");
    private final JButton loadGame = ButtonManager.createGameButton("Load Game");
    private final JButton settingsButton = ButtonManager.createGameButton("Settings");
    private final JButton quitGame = ButtonManager.createGameButton("Quit");


    /**
     * Constructeur du panneau de menu.
     * <p>
     * Initialise l'interface graphique en utilisant un {@link BoxLayout} vertical.
     * Les boutons sont centrés et espacés proportionnellement au niveau de zoom global du jeu.
     * Lance également la lecture de la musique "Menu Theme".
     * </p>
     */
    public MenuPanel() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        double zoom = GamePanel.ZOOM;
        int strut = (int) (10 * zoom);

        this.add(Box.createVerticalGlue());
        this.add(newGame);
        this.add(Box.createVerticalStrut(strut));
        this.add(loadGame);
        this.add(Box.createVerticalStrut(strut));
        this.add(settingsButton);
        this.add(Box.createVerticalStrut(strut));
        this.add(quitGame);
        this.add(Box.createVerticalStrut(strut));
        this.add(Box.createVerticalGlue());

        SoundManager.playMusic("Menu Theme");
    }

    /**
     * Dessine l'arrière-plan du menu.
     * <p>
     * L'image de fond est redimensionné dynamiquement pour couvrir toute la surface du panneau,
     * quelle que soit la taille de la fenêtre
     * </p>
     * @param g L'objet Graphics utilisé pour le dessin.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Associe un écouteur d'événement au bouton "New Game".
     *
     * @param listener L'action à exécuter lors du clic (lancement d'une nouvelle partie).
     */
    public void addNewGameListener(ActionListener listener) {
        newGame.addActionListener(listener);
    }

    /**
     * Associe un écouteur d'événements au bouton "Load Game".
     *
     * @param listener L'action à exécuter lors du clic (chargement d'une sauvegarde).
     */
    public void addLoadGameListener(ActionListener listener) {
        loadGame.addActionListener(listener);
    }

    /**
     * Associe un écouteur d'événements au bouton "Quit".
     *
     * @param listener L'action à exécuté lors du clic (fermeture de l'application).
     */
    public void addQuitterListener(ActionListener listener) {
        quitGame.addActionListener(listener);
    }

    /**
     * Associe un écouteur d'événements au bouton "Settings".
     *
     * @param listener L'action à exécuter lors du clic (ouverture du panneau de paramètres).
     */
    public void addSettingsListener(ActionListener listener) {
        settingsButton.addActionListener(listener);
    }
}