package upc.view.gui;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Panneau affiché lorsque le jeu est en pause.
 * <p>
 * Cet overlay agis comme une superposition semi transparente sur le {@link GamePanel}.
 * Il bloque les interactions avec le jeu et propose les options suivantes :
 * <ul>
 * <li><b>Continue</b> : Reprendre la partie en cours.</li>
 * <li><b>Settings</b> : Accéder aux réglages.</li>
 * <li><b>Menu</b> : Retourner au menu principal.</li>
 * <li><b>Quit</b> : Quitter l'application.</li>
 * </ul>
 */
public class PausePanel extends JPanel {

    private int menuX = (int) (20 * GamePanel.ZOOM);
    private int menuY = (int) (15 * GamePanel.ZOOM);
    private int menuWidth = (int) (250 * GamePanel.ZOOM);
    private int menuHeight = (int) ((GamePanel.DEFAULT_HEIGHT - 300) * GamePanel.ZOOM);

    private final JButton resumeButton;
    private final JButton saveButton;
    private final JButton settingsButton;
    private final JButton mainMenuButton;
    private final JButton quitButton;

    /**
     * Constructeur du panneau de pause.
     * <p>
     * Initialise les boutons via {@link ButtonManager}, configure le panneau pour être invisible
     * par défaut et définit un layout nul (positionnement absolu).
     * </p>
     */
    public PausePanel() {
        this.setLayout(null);
        this.setOpaque(false);
        this.setVisible(false);

        resumeButton = ButtonManager.createGameButton("Continue");
        saveButton = ButtonManager.createGameButton("Save Game");
        settingsButton = ButtonManager.createGameButton("Settings");
        mainMenuButton = ButtonManager.createGameButton("Return to menu");
        quitButton = ButtonManager.createGameButton("Quit Game");

        initButtons();
    }

    /**
     * Dessine l'arrière-plan du menu de pause.
     * <p>
     * Cette méthode réalise deux rendus graphiques :
     * <ol>
     * <li>Un voile noir semi-transparent sur toute la fenêtre pour assombrir le jeu.</li>
     * <li>Un cadre rectangulaire contenant les boutons.</li>
     * </ol>
     *
     * @param g L'objet Graphics contextuel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(250, 250, 250, 100));
        g2.fillRect(menuX, menuY, menuWidth + 10, menuHeight + 10);
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(menuX + 5, menuY + 5, menuWidth, menuHeight);
    }

    /**
     * Calcule la position des boutons et les ajoute au panneau.
     * <p>
     * Les boutons sont empilés verticalement et centrés horizontalement par rapport
     * au cadre du menu défini par {@code menuX} et {@code menuWidth}.
     * </p>
     */
    private void initButtons() {
        int btnWidth = (int) (200 * GamePanel.ZOOM);
        int btnHeight = (int) (50 * GamePanel.ZOOM);

        // Pour centrer et espacer les boutons
        int btnX = menuX + (menuWidth - btnWidth) / 2 + 5;
        int btnY = menuY + 50;
        int gap = (int) (20 * GamePanel.ZOOM) + btnHeight;

        resumeButton.setBounds(btnX, btnY, btnWidth, btnHeight);
        saveButton.setBounds(btnX, btnY + gap, btnWidth, btnHeight);
        settingsButton.setBounds(btnX, btnY + gap * 2, btnWidth, btnHeight);
        mainMenuButton.setBounds(btnX, btnY + gap * 3, btnWidth, btnHeight);
        quitButton.setBounds(btnX, btnY + gap * 4, btnWidth, btnHeight);

        saveButton.setFocusable(false);

        this.add(resumeButton);
        this.add(saveButton);
        this.add(settingsButton);
        this.add(mainMenuButton);
        this.add(quitButton);
    }


    /**
     * Récupère le bouton de reprise du jeu.
     * @return Le bouton "Continue".
     */
    public JButton getResumeButton() {
        return resumeButton;
    }

    /**
     * Récupère le bouton de sauvegarde du jeu.
     * @return Le bouton "Save Game".
     */
    public JButton getSaveButton() {
        return saveButton;
    }

    /**
     * Récupère le bouton de retour au menu principal.
     * @return Le bouton "Return to menu".
     */
    public JButton getMainMenuButton() {
        return mainMenuButton;
    }

    /**
     * Récupère le bouton d'accès aux paramètre.
     * @return Le bouton "Settings".
     */
    public JButton getSettingsButton() {
        return settingsButton;
    }

    /**
     * Récupère le bouton permettant de quitter le jeu.
     * @return Le bouton "Quit Game".
     */
    public JButton getQuitButton() {
        return quitButton;
    }

}